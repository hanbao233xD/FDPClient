package org.cef.browser;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * @author Feather Client Team
 */
public interface ICefRenderer {
    void render();

    void destroy();

    void onPaint(boolean var1, Rectangle[] var2, ByteBuffer var3, int var4, int var5);

    void onPopupSize(Rectangle var1);

    void onPopupClosed();
}

