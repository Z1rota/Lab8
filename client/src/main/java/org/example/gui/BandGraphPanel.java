package org.example.gui;

import org.example.mainClasses.Coordinates;
import org.example.mainClasses.MusicBand;
import org.example.network.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class BandGraphPanel extends JPanel {
    private MainFrame mainFrame;
    private GuiCommandManager commandManager;
    private List<MusicBand> musicBands;
    private Map<Long, Float> animationStates = new HashMap<>();
    private Timer animationTimer;
    private List<BandPosition> bandPositions = new ArrayList<>();
    private Timer updateTimer;
    private Set<Long> lastKnownIds = new HashSet<>();

    // Границы графика
    private static final int MIN_X = -3000;
    private static final int MAX_X = 3000;
    private static final int MIN_Y = -3000;
    private static final int MAX_Y = 3000;
    private static final int STEP = 500;

    private class BandPosition {
        int x, y;
        boolean visible;
        MusicBand band;
        float size;

        BandPosition(int x, int y, boolean visible, MusicBand band, float size) {
            this.x = x;
            this.y = y;
            this.visible = visible;
            this.band = band;
            this.size = size;
        }
    }

    public BandGraphPanel(MainFrame mainFrame, GuiCommandManager commandManager) {
        this.mainFrame = mainFrame;
        this.commandManager = commandManager;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                startInitialAnimation();
                startAutoUpdate();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                stopAutoUpdate();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margin = 70;
        int width = getWidth() - 2 * margin;
        int height = getHeight() - 2 * margin;

        drawAxes(g2, margin, width, height);

        if (musicBands != null && !musicBands.isEmpty()) {
            drawMusicBands(g2, margin, width, height);
        }
    }

    private void drawAxes(Graphics2D g2, int margin, int width, int height) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));

        int centerX = margin + width / 2;
        int centerY = margin + height / 2;

        g2.drawLine(margin, centerY, margin + width, centerY); // Ось X
        g2.drawLine(centerX, margin, centerX, margin + height); // Ось Y

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(mainFrame.getLocalizedString("graph.x_axis"), margin + width - 10, centerY - 10);
        g2.drawString(mainFrame.getLocalizedString("graph.y_axis"), centerX + 10, margin + 15);

        g2.setFont(new Font("Arial", Font.PLAIN, 10));

        // Разметка оси X
        for (int x = MIN_X; x <= MAX_X; x += STEP) {
            if (x == 0) continue;
            int xPos = margin + width/2 + (int)((float)x / (MAX_X - MIN_X) * width);
            g2.drawLine(xPos, centerY - 5, xPos, centerY + 5);
            g2.drawString(String.valueOf(x), xPos - 15, centerY + 20);
        }

        // Разметка оси Y
        for (int y = MIN_Y; y <= MAX_Y; y += STEP) {
            if (y == 0) continue;
            int yPos = margin + height/2 - (int)((float)y / (MAX_Y - MIN_Y) * height);
            g2.drawLine(centerX - 5, yPos, centerX + 5, yPos);
            g2.drawString(String.valueOf(y), centerX - 50, yPos + 5);
        }
    }

    // Остальные методы остаются без изменений, кроме использования локализованных строк
    // ...

    public void updateLocalization() {
        // Перерисовываем панель с новыми подписями
        repaint();
    }



    private void drawMusicBands(Graphics2D g2, int margin, int width, int height) {
        bandPositions.clear();
        int centerX = margin + width / 2;
        int centerY = margin + height / 2;
        String currentUser = mainFrame.getUser() != null ? mainFrame.getUser().getLogin() : null;

        // Определяем новые элементы
        Set<Long> currentIds = new HashSet<>();
        for (MusicBand band : musicBands) {
            currentIds.add(band.getId());
            if (!lastKnownIds.contains(band.getId())) {
                // Новый элемент - запускаем для него анимацию
                animationStates.put(band.getId(), 0f);
            }
        }
        lastKnownIds = currentIds;

        for (MusicBand band : musicBands) {
            Coordinates coord = band.getCoordinates();

            int realX = centerX + (int)((float)coord.getX() / (MAX_X - MIN_X) * width);
            int realY = centerY - (int)((float)coord.getY() / (MAX_Y - MIN_Y) * height);

            boolean isVisible = realX >= margin && realX <= margin + width &&
                    realY >= margin && realY <= margin + height;

            int drawX = isVisible ? realX : (realX < margin ? margin + 10 : margin + width - 10);
            int drawY = isVisible ? realY : (realY < margin ? margin + 10 : margin + height - 10);

            // Получаем прогресс анимации для этого элемента (1 = полный размер)
            float animProgress = animationStates.getOrDefault(band.getId(), 1f);
            float size = 5 + 15 * animProgress;

            bandPositions.add(new BandPosition(realX, realY, isVisible, band, size));

            Color color = generateUserColor(band.getUserLogin());
            if (band.getUserLogin() != null && band.getUserLogin().equals(currentUser)) {
                color = color.brighter();
            }

            g2.setColor(color);
            drawMusicNote(g2, drawX, drawY, size, color, !isVisible);
        }
    }

    private void startInitialAnimation() {
        // Анимация только для элементов, уже существующих при открытии
        if (musicBands != null) {
            for (MusicBand band : musicBands) {
                animationStates.put(band.getId(), 0f);
            }
        }

        startAnimationTimer();
    }

    private void startAnimationForNewElements() {
        // Автоматически вызывается при обновлении данных
        startAnimationTimer();
    }

    private void startAnimationTimer() {
        if (animationTimer != null && animationTimer.isRunning()) {
            return;
        }

        animationTimer = new Timer(20, e -> {
            boolean hasAnimations = false;

            // Обновляем прогресс анимации для всех элементов
            for (Map.Entry<Long, Float> entry : animationStates.entrySet()) {
                float progress = entry.getValue() + 0.03f;
                if (progress < 1f) {
                    entry.setValue(progress);
                    hasAnimations = true;
                } else {
                    entry.setValue(1f);
                }
            }

            if (!hasAnimations) {
                animationTimer.stop();
            }
            repaint();
        });
        animationTimer.start();
    }

    private Color generateUserColor(String userLogin) {
        if (userLogin == null) return Color.GRAY;

        // Стабильная генерация цвета на основе хеша логина
        int hash = userLogin.hashCode();
        float hue = Math.abs(hash % 1000) / 1000.0f;
        float saturation = 0.7f;
        float brightness = 0.8f;

        return Color.getHSBColor(hue, saturation, brightness);
    }

    private void drawMusicNote(Graphics2D g2, int x, int y, float size, Color color, boolean isOutOfBounds) {
        // Основной круг
        g2.fillOval((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);

        // "Хвостик" ноты
        g2.setStroke(new BasicStroke(size/5));
        g2.drawLine((int)(x + size/2), (int)y, (int)(x + size), (int)(y - size));

        if (isOutOfBounds) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.drawString("→", x + (int)(size*0.8), y);
        }
    }

    private void handleClick(int clickX, int clickY) {
        BandPosition closest = null;
        double minDistance = Double.MAX_VALUE;

        for (BandPosition pos : bandPositions) {
            double distance = Math.sqrt(Math.pow(clickX - pos.x, 2) + Math.pow(clickY - pos.y, 2));
            if (distance < pos.size * 1.5 && distance < minDistance) {
                minDistance = distance;
                closest = pos;
            }
        }

        if (closest != null) {
            commandManager.showBandInfo(closest.band);
        }
    }

    public void updateGraph(List<MusicBand> bands) {
        List<MusicBand> oldBands = this.musicBands;
        this.musicBands = bands;

        // Если это не первая загрузка, проверяем новые элементы
        if (oldBands != null) {
            startAnimationForNewElements();
        }
    }

    private void startAutoUpdate() {
        if (updateTimer != null && updateTimer.isRunning()) {
            return;
        }

        updateTimer = new Timer(3000, e -> {
            commandManager.updateTableData();
        });
        updateTimer.start();
    }

    private void stopAutoUpdate() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        stopAutoUpdate();
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

}