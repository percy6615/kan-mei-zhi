package com.lineage.config.tool;

import java.awt.Image;
import java.util.Map;

import javax.swing.ImageIcon;

import javolution.util.FastMap;

/**
 * 存放程序用图像
 */
public final class ImageTable {
    private static final Map<String, Image> ICONS = new FastMap<String, Image>();

    private static final Map<String, ImageIcon> IMAGES = new FastMap<String, ImageIcon>();
    private static final String IMAGES_DIRECTORY = "./data/image/";

    public static Image getIcon(String name) {
        if (!ICONS.containsKey(name)) {
            ICONS.put(name, new ImageIcon(IMAGES_DIRECTORY + name).getImage());
        }
        return (Image) ICONS.get(name);
    }

    public static ImageIcon getImage(String name) {
        if (!IMAGES.containsKey(name)) {
            IMAGES.put(name, new ImageIcon(IMAGES_DIRECTORY + name));
        }
        return (ImageIcon) IMAGES.get(name);
    }
}