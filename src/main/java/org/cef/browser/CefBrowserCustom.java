package org.cef.browser;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import org.cef.CefClient;
import org.cef.callback.CefDragData;
import org.cef.handler.CefRenderHandler;
import org.cef.handler.CefScreenInfo;

/**
 * CefBrowserOsr but with custom rendering
 * @see org.cef.browser.CefBrowser_N is fucking package private
 * @author Feather Client Team, modified by Liulihaocai
 */
public class CefBrowserCustom extends CefBrowser_N implements CefRenderHandler {
    private final ICefRenderer renderer_;
//    private final long window_handle_ = 0L;
    private boolean justCreated_ = false;
    // TODO make [browser_rect_] with right value
    private final Rectangle browser_rect_ = new Rectangle(0, 0, 1, 1);
    private final Point screenPoint_ = new Point(0, 0);
    private final double scaleFactor_ = 1.0;
    private final int depth = 32;
    private final int depth_per_component = 8;
    private final boolean isTransparent_;
    private final Component dc_ = new Component(){};

    public CefBrowserCustom(CefClient client, String url, boolean transparent, CefRequestContext context, ICefRenderer renderer) {
        this(client, url, transparent, context, renderer, null, null);
    }

    public CefBrowserCustom(CefClient client, String url, boolean transparent, CefRequestContext context, ICefRenderer renderer, CefBrowserCustom parent, Point inspectAt) {
        super(client, url, context, parent, inspectAt);
        this.isTransparent_ = transparent;
        this.renderer_ = renderer;
    }

    @Override
    public void createImmediately() {
        this.justCreated_ = true;
        this.createBrowserIfRequired(false);
    }

    @Override
    public Component getUIComponent() {
        return this.dc_;
    }

    @Override
    public CefRenderHandler getRenderHandler() {
        return this;
    }

    @Override
    protected CefBrowser_N createDevToolsBrowser(CefClient client, String url, CefRequestContext context, CefBrowser_N parent, Point inspectAt) {
        return new CefBrowserCustom(client, url, this.isTransparent_, context, null, this, inspectAt);
    }

    private synchronized long getWindowHandle() {
        return 0L;
    }

    @Override
    public Rectangle getViewRect(CefBrowser browser) {
        return this.browser_rect_;
    }

    @Override
    public Point getScreenPoint(CefBrowser browser, Point viewPoint) {
        Point screenPoint = new Point(this.screenPoint_);
        screenPoint.translate(viewPoint.x, viewPoint.y);
        return screenPoint;
    }

    @Override
    public void onPopupShow(CefBrowser browser, boolean show) {
        if (!show) {
            this.renderer_.onPopupClosed();
            this.invalidate();
        }
    }

    @Override
    public void onPopupSize(CefBrowser browser, Rectangle size) {
        this.renderer_.onPopupSize(size);
    }

    @Override
    public void onPaint(CefBrowser browser, boolean popup, Rectangle[] dirtyRects, ByteBuffer buffer, int width, int height) {
        this.renderer_.onPaint(popup, dirtyRects, buffer, width, height);
    }

    @Override
    public boolean onCursorChange(CefBrowser browser, int cursorType) {
        return true;
    }

    @Override
    public boolean startDragging(CefBrowser browser, CefDragData dragData, int mask, int x, int y) {
        return false;
    }

    @Override
    public void updateDragCursor(CefBrowser browser, int operation) {
    }

    private void createBrowserIfRequired(boolean hasParent) {
        long windowHandle = 0L;
        if (hasParent) {
            windowHandle = this.getWindowHandle();
        }
        if (this.getNativeRef("CefBrowser") == 0L) {
            if (this.getParentBrowser() != null) {
                this.createDevTools(this.getParentBrowser(), this.getClient(), windowHandle, true, this.isTransparent_, null, this.getInspectAt());
            } else {
                this.createBrowser(this.getClient(), windowHandle, this.getUrl(), true, this.isTransparent_, null, this.getRequestContext());
            }
        } else if (hasParent && this.justCreated_) {
            this.notifyAfterParentChanged();
            this.setFocus(true);
            this.justCreated_ = false;
        }
    }

    private void notifyAfterParentChanged() {
        this.getClient().onAfterParentChanged(this);
    }

    @Override
    public boolean getScreenInfo(CefBrowser browser, CefScreenInfo screenInfo) {
        screenInfo.Set(this.scaleFactor_, this.depth, this.depth_per_component, false, this.browser_rect_.getBounds(), this.browser_rect_.getBounds());
        return true;
    }

    @Override
    public CompletableFuture<BufferedImage> createScreenshot(boolean nativeResolution) {
        return null;
    }

    @Override
    public synchronized void onBeforeClose() {
        renderer_.destroy();
    }

    //    @Override
//    public void wasResized(int width, int height) {
//        this.browser_rect_.setBounds(0, 0, width, height);
//        super.wasResized(width, height);
//    }
}
