package org.cef.browser.lwjgl;

import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.cef.browser.ICefRenderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * from https://www.feathermc.com/
 */
public class CefRendererLwjgl implements ICefRenderer {

    private static final FloatBuffer FLOAT_BUFFER;

    static {
        float[] fArray = new float[]{0.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f};
        FLOAT_BUFFER = BufferUtils.createFloatBuffer(fArray.length);
        FLOAT_BUFFER.put(fArray, 0, fArray.length);
        FLOAT_BUFFER.rewind();
    }

    private boolean initialized = false;
    protected int theWidth = 0;
    protected int theHeight = 0;
    protected Rectangle rect = new Rectangle(0, 0, 0, 0);
    protected Rectangle modifyRect = new Rectangle(0, 0, 0, 0);
    protected int texture = 0;

    protected void initialize() {
        this.texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    }

    @Override
    public void destroy() {
        if (this.texture != 0) {
            GL11.glDeleteTextures(this.texture);
            this.texture = 0;
        }
    }

    @Override
    public void render() {
        if (!(this.initialized && this.theWidth != 0 && this.theHeight != 0)) {
            return;
        }
        int n = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int n2 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE);
        int n3 = GL11.glGetInteger(GL11.GL_UNPACK_ALIGNMENT);
        GL11.glPushAttrib(61440);
        GL11.glPushClientAttrib(GL11.GL_CLIENT_VERTEX_ARRAY_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glClear(GL11.GL_ACCUM);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glInterleavedArrays(10791, 0, (FloatBuffer) FLOAT_BUFFER);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, n3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, n);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        GL11.glPopClientAttrib();
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, n2);
    }

    @Override
    public void onPaint(boolean popup, Rectangle[] rectangleArray, ByteBuffer byteBuffer, int width, int height) {
        if (popup && this.modifyRect.width > 0 && this.modifyRect.height > 0) {
            return;
        }
        int n3 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        boolean originalGlBlend = GL11.glIsEnabled(GL11.GL_BLEND);
        if (!originalGlBlend) {
            GL11.glEnable(GL11.GL_BLEND);
        }
        boolean originalGlTex2d = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        if (!originalGlTex2d) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        if (!this.initialized) {
            this.initialize();
            this.initialized = true;
        } else {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
        }
        int n4 = GL11.glGetInteger(GL11.GL_UNPACK_ALIGNMENT);
        int n5 = GL11.glGetInteger(GL11.GL_UNPACK_ROW_LENGTH);
        int n6 = GL11.glGetInteger(GL11.GL_UNPACK_SKIP_PIXELS);
        int n7 = GL11.glGetInteger(GL11.GL_UNPACK_SKIP_ROWS);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        if (!popup) {
            int widthCache = this.theWidth;
            int heightCache = this.theHeight;
            this.theWidth = width;
            this.theHeight = height;
            GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, this.theWidth);
            if (widthCache != this.theWidth || heightCache != this.theHeight) {
                GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
                GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.theWidth, this.theHeight, 0, 32993, 33639, (ByteBuffer)byteBuffer);
            } else {
                for (Rectangle rectangle : rectangleArray) {
                    GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, rectangle.x);
                    GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, rectangle.y);
                    GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, rectangle.x, rectangle.y, rectangle.width, rectangle.height, 32993, 33639, (ByteBuffer)byteBuffer);
                }
            }
        } else {
            int n10 = 0;
            int n11 = 0;
            int n12 = this.modifyRect.x;
            int n13 = this.modifyRect.y;
            int n14 = width;
            int n15 = height;
            if (n12 < 0) {
                n10 = -n12;
                n12 = 0;
            }
            if (n13 < 0) {
                n11 = -n13;
                n13 = 0;
            }
            if (n12 + n14 > this.theWidth) {
                n14 -= n12 + n14 - this.theWidth;
            }
            if (n13 + n15 > this.theHeight) {
                n15 -= n13 + n15 - this.theHeight;
            }
            GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, width);
            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, n10);
            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, n11);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, n12, n13, n14, n15, 32993, 33639, (ByteBuffer)byteBuffer);
        }
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, n4);
        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, n5);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, n6);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, n7);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, n3);
        if (!originalGlBlend) {
            GL11.glDisable(GL11.GL_BLEND);
        }
        if (!originalGlTex2d) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
    }

    @Override
    public void onPopupSize(Rectangle rectangle) {
        if (rectangle.width <= 0 || rectangle.height <= 0) {
            return;
        }
        this.rect = rectangle;
        this.modifyRect = this.processRect(this.rect);
    }

    @Override
    public void onPopupClosed() {
        this.rect.setBounds(0, 0, 0, 0);
        this.modifyRect.setBounds(0, 0, 0, 0);
    }

    protected Rectangle processRect(Rectangle rectangle) {
        if (rectangle.x < 0) {
            rectangle.x = 0;
        }
        if (rectangle.y < 0) {
            rectangle.y = 0;
        }
        if (rectangle.x + rectangle.width > this.theWidth) {
            rectangle.x = this.theWidth - rectangle.width;
        }
        if (rectangle.y + rectangle.height > this.theHeight) {
            rectangle.y = this.theHeight - rectangle.height;
        }
        if (rectangle.x < 0) {
            rectangle.x = 0;
        }
        if (rectangle.y < 0) {
            rectangle.y = 0;
        }
        return rectangle;
    }
}