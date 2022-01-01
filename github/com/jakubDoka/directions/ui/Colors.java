package github.com.jakubDoka.directions.ui;
import java.util.HashMap;
import java.awt.Color;

/**
 * Handles all operations related to colors. It allows storing colors as opaque integers,
 * and only materializing them when needed to reduce gc overhead.
 */
public class Colors {
    private static final HashMap<ColorID, Color> storage = new HashMap<>(1024);
    private static final ColorID access = new ColorID(0);

    /**
     * Returns a color of the given value from pool if possible.
     * @param value
     * @return
     */
    public static Color create(int value) {
        access.setValue(value);
        Color color = storage.get(access);

        if (color == null) {
            color = new Color(value, true);
            storage.put(new ColorID(value), color);
        }

        return color;
    }

    /**
     * Returns a color components combined together, method 
     * assumes components will be in range 0-255.
     */
    public static int color(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Returns 'a' color component of color.
     */
    public static int a(int color) {
        return (color >> 24) & 0xFF;
    }

    /**
     * Returns 'r' color component of color.
     */
    public static int r(int color) {
        return (color >> 16) & 0xFF;
    }

    /**
     * Returns 'g' color component of color.
     */
    public static int g(int color) {
        return (color >> 8) & 0xFF;
    }

    /**
     * Returns 'b' color component of color.
     */
    public static int b(int color) {
        return color & 0xFF;
    }

    /**
     * Multiplies two color channels.
     */
    public static int mulComp(int a, int b) {
        return (a * b) >> 8;
    }

    /**
     * Linear interpolation on a color channel.
     * 
     * @param a - origin color channel
     * @param b - target color channel
     * @param t - interpolation value
     */
    public static int lerpComp(int a, int b, int t) {
        return Colors.mulComp((b - a), t) + a;
    }

    /**
     * Linear interpolation of two colors.
     * Undefined behavior when t is out of range.
     * 
     * @param a - origin color
     * @param b - target color
     * @param t - interpolation value, 0-1
     * 
     */
    public static int lerp(int a, int b, int t) {
        return Colors.color(
            Colors.lerpComp(Colors.r(a), Colors.r(b), t),
            Colors.lerpComp(Colors.g(a), Colors.g(b), t),
            Colors.lerpComp(Colors.b(a), Colors.b(b), t),
            Colors.lerpComp(Colors.a(a), Colors.a(b), t)
        );
    }

    /**
     * Color ID exist purely because we dont want to allocate Integer each time
     * we lookup color. That would defeat whole purpose of color storage.
     */
    private static class ColorID {
        int value;

        public ColorID(int value) {
            this.value = value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ColorID) {
                return ((ColorID)obj).value == this.value;
            }
            return false;
        }
    }
}
