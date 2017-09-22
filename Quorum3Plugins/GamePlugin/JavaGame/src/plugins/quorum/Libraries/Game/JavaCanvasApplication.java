/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.quorum.Libraries.Game;

import quorum.Libraries.Game.Game_;
import quorum.Libraries.Game.DesktopConfiguration_;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.PaintEvent;

import javax.swing.SwingUtilities;

//import org.lwjgl.LWJGLException;
//import org.lwjgl.opengl.AWTGLCanvas;
//import org.lwjgl.opengl.Display;
//import org.lwjgl.opengl.PixelFormat;
import plugins.quorum.Libraries.Game.Graphics.GraphicsManager;

/**
 *
 * @author alleew
 */
public class JavaCanvasApplication 
{
    public java.lang.Object me_ = null;
    
    public JavaCanvasDisplay display;

    Game_ game;
//    AWTGLCanvas canvas;
    
    boolean running = true;
    boolean exitRequested = false;
    int lastWidth;
    int lastHeight;

    final String logTag = "LwjglAWTCanvas";

    public DesktopConfiguration_ config = null;
    
    static int instanceCount;
    
    public void SetupNative(Game_ game, DesktopConfiguration_ config)
    {
        instanceCount++;
        
        this.game = game;
        this.config = config;
        
        try
        {
//            canvas = new AWTGLCanvas(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(),
//                new PixelFormat(), null)
//            {
//                private final Dimension minSize = new Dimension(0, 0);
//                private final NonSystemPaint nonSystemPaint = new NonSystemPaint(this);
//                
//                @Override
//                public Dimension getMinimumSize()
//                {
//                    return minSize;
//                }
//                
//                @Override
//                public void initGL()
//                {
//                    Create();
//                }
//                
//                @Override
//                public void paintGL()
//                {
//                    try
//                    {
//                        boolean systemPaint = !(EventQueue.getCurrentEvent() instanceof NonSystemPaint);
//                        Render(systemPaint);
//                        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(nonSystemPaint);
//                    }
//                    catch (Throwable ex)
//                    {
//                        Exception(ex);
//                    }
//                }
//            };
        }
        catch (Throwable ex)
        {
            Exception(ex);
            return;
        }
        
//        canvas.setBackground(new Color((float)config.Get_Libraries_Game_DesktopConfiguration__initialBackgroundColor_().GetRed(),
//            (float)config.Get_Libraries_Game_DesktopConfiguration__initialBackgroundColor_().GetGreen(),
//            (float)config.Get_Libraries_Game_DesktopConfiguration__initialBackgroundColor_().GetBlue(),
//            (float)config.Get_Libraries_Game_DesktopConfiguration__initialBackgroundColor_().GetAlpha()));
//        
//        display = ((quorum.Libraries.Game.JavaCanvasDisplay)((quorum.Libraries.Game.JavaCanvasApplication)me_).canvasDisplay).plugin_;
//        display.canvas = canvas;
    }
    
    public Game_ GetGame()
    {
        return game;
    }
    
    public Canvas GetCanvas()
    {
        return null;//canvas;
    }
    
    public void Create()
    {
//        try
//        {
//            SetGlobals();
//            
//            display = ((quorum.Libraries.Game.JavaCanvasDisplay)GameStateManager.display).plugin_;
//            
//            display.InitiateGLInstances();
//            canvas.setVSyncEnabled(config.Get_Libraries_Game_DesktopConfiguration__vSyncEnabled_());
//            game.InitializeLayers();
//            game.CreateGame();
//            lastWidth = Math.max(1, canvas.getWidth());
//            lastHeight = Math.max(1, canvas.getHeight());
//            Start();
//        }
//        catch (Throwable ex)
//        {
//            Stopped();
//            Exception(ex);
//        }
    }
    
    void Render (boolean shouldRender)// throws LWJGLException
    {
        /*
        if (!running)
            return;
        
        SetGlobals();

        int width = Math.max(1, canvas.getWidth());
        int height = Math.max(1, canvas.getHeight());
        if (lastWidth != width || lastHeight != height)
        {
            lastWidth = width;
            lastHeight = height;
            //resize(width, height);
            //game.Resize(width, height);
            shouldRender = true;
        }
        
        GameStateManager.nativeGraphics.SetDrawingRegion(0, 0, lastWidth, lastHeight);
        
        if (exitRequested)
        {
            Stop();
            exitRequested = false;
        }
        
        if (!running)
            return;
        
        shouldRender |= display.ShouldRender();
        
        if (shouldRender)
        {
            display.UpdateTime();
            
            game.ContinueGame();
            canvas.swapBuffers();
            System.out.flush();
        }
        
        Display.sync(GetFrameRate() * instanceCount);
        */
    }

    protected int GetFrameRate () 
    {
        int frameRate = IsActive() ? config.Get_Libraries_Game_DesktopConfiguration__foregroundFPS_() : config.Get_Libraries_Game_DesktopConfiguration__backgroundFPS_();
        if (frameRate == -1)
            frameRate = 10;
        if (frameRate == 0)
            frameRate = config.Get_Libraries_Game_DesktopConfiguration__backgroundFPS_();
        if (frameRate == 0)
            frameRate = 30;
        return frameRate;
    }
    
    /** Returns true when the frame containing the canvas is the foreground window. */
    public boolean IsActive () 
    {
//        Component root = SwingUtilities.getRoot(canvas);
//        return root instanceof Frame ? ((Frame)root).isActive() : true;
        return false;
    }
    
    /** Called after {@link ApplicationListener} create and resize, but before the game loop iteration. */
    protected void Start() 
    {
        
    }

    /** Called when the canvas size changes. */
    protected void Resize(int width, int height) 
    {
        
    }

    /** Called when the game loop has stopped. */
    protected void Stopped() 
    {
        
    }
    
    public void Stop()
    {
        if (!running)
            return;
        
        running = false;
        SetGlobals();
        //Array<LifecycleListener> listeners = lifecycleListeners;
        
        // To allow destroying of OpenGL textures during disposal.
//        if (canvas.isDisplayable())
//        {
//            MakeCurrent();
//        }
//        else
//        {
//            //error(logTag, "OpenGL context destroyed before application listener has had a chance to dispose of textures.");
//        }
        
//        synchronized (listeners)
//        {
//            for (LifecycleListener listener: listeners)
//            {
//                listener.pause();
//                listener.dispose();
//            }
//        }
        //game.Pause();
        //game.Dispose();
        
        GameStateManager.application = null;
        GameStateManager.display = null;
        
        instanceCount--;
        
        Stopped();
    }

    public void Exit() 
    {
        exitRequested = true;
    }

    /** Make the canvas' context current. It is highly recommended that the context is only made current inside the AWT thread (for
     * example in an overridden paintGL()). */
    public void MakeCurrent () 
    {
        try 
        {
//            canvas.makeCurrent();
            SetGlobals();
        }
        catch (Throwable ex) 
        {
            Exception(ex);
        }
    }

    /** Test whether the canvas' context is current. */
    public boolean IsCurrent() 
    {
        try 
        {
//            return canvas.isCurrent();
        }
        catch (Throwable ex) 
        {
            Exception(ex);
            return false;
        }
        return false;
    }
    
    protected void Exception(Throwable ex) 
    {
        ex.printStackTrace();
        Stop();
    }
    
    public void SetGlobals()
    {
        ((quorum.Libraries.Game.JavaCanvasApplication)me_).SetGlobals();
    }
    
    public void SetGlobalsNative(quorum.Libraries.Game.JavaCanvasDisplay_ quorumDisplay)
    {
        display = ((quorum.Libraries.Game.JavaCanvasDisplay)quorumDisplay).plugin_;
    }
    
//    static public class NonSystemPaint extends PaintEvent 
//    {
//        public NonSystemPaint (AWTGLCanvas canvas) 
//        {
//            super(canvas, UPDATE, new Rectangle(0, 0, 99999, 99999));
//        }
//    }
}
