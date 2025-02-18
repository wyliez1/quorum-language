package plugins.quorum.Libraries.Game;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import java.util.HashMap;

import android.view.ScaleGestureDetector;
import android.view.inputmethod.InputMethodManager;
import plugins.quorum.Libraries.Interface.Accessibility.AndroidAccessibility;
import plugins.quorum.Libraries.Interface.Mobile.AndroidKeyboard;
import quorum.Libraries.Containers.List_;
import quorum.Libraries.Interface.Controls.*;
import quorum.Libraries.Interface.Events.GestureEvent;
import quorum.Libraries.Interface.Events.GestureEvent_;
import quorum.Libraries.Interface.Events.TouchEvent;
import quorum.Libraries.Interface.Events.TouchEvent_;
import quorum.Libraries.Interface.Item_;

/**
 *
 * @author alleew
 */
public class AndroidInput 
{
    private final String DEBUG_TAG = "AndroidInput";
    public java.lang.Object me_ = null;

    public static final int GESTURE_SINGLE_TAP = 1;
    public static final int GESTURE_DOUBLE_TAP = 2;
    public static final int GESTURE_FLING = 3;
    public static final int GESTURE_LONG_PRESS = 4;
    public static final int GESTURE_SCROLL = 5;
    public static final int GESTURE_SCALE_BEGIN = 6;
    public static final int GESTURE_SCALE_CONTINUE = 7;
    public static final int GESTURE_SCALE_FINISH = 8;

    private List_ touchEvents;
    private List_ gestureEvents;
    private final HashMap<Integer, Coordinates> map = new HashMap<>();

    public final AndroidKeyboard androidKeyboard = new AndroidKeyboard();

    boolean longPressed = false;
    int maxFingerCount;
    int currentFingerCount;
    float scaleFactor;

    public void InitializePlugin(List_ list, List_ gestureList)
    {
        touchEvents = list;
        gestureEvents = gestureList;
    }
    
    public void AddEvent(TouchEvent_ event)
    {
        touchEvents.Add(event);
    }
    
    public void AddEvent(MotionEvent event)
    {
        TouchEvent_[] events = ConvertToQuorumEvents(event);
        for (TouchEvent_ e : events)
            AddEvent(e);
    }
    
    public TouchEvent_[] ConvertToQuorumEvents(MotionEvent e)
    {
        if (e == null) {
            return new TouchEvent_[0];
        }
            TouchEvent_[] eventArray;
            TouchEvent event;
            int id;
            int x;
            int y;
            Coordinates coordinates;
            
            Item_ temp =  AndroidAccessibility.lastSpokenChild;
            switch(e.getActionMasked())
            {
                    case (MotionEvent.ACTION_DOWN):
                    case (MotionEvent.ACTION_POINTER_DOWN):
                        if(temp != null) {
                            if(temp.GetAccessibilityCode() == temp.Get_Libraries_Interface_Item__BUTTON_()) {
                                ((Button_) temp).ClickedMouse();
                                AndroidAccessibility.sendAccessibilityEventForVirtualView("Clicked the button");
                            } else if(temp.GetAccessibilityCode() == temp.Get_Libraries_Interface_Item__TEXTBOX_()){
                                ((TextBox_)temp).Focus();
                                AndroidAccessibility.sendAccessibilityEventForVirtualView(((TextBox_)temp).GetText());
                                TextField_ textField = ((TextField_)temp);
                                androidKeyboard.DisplayKeyboard();
                            } else if(temp.GetAccessibilityCode() == temp.Get_Libraries_Interface_Item__TREE_ITEM_()){
                                ((TreeItem_)temp).Focus();
                                ((TreeItem_)temp).ClickedMouse();
                                if (((TreeItem_)temp).IsOpen())
                                    ((TreeItem_)temp).Close();
                                else
                                    ((TreeItem_)temp).Open();
                            } else if(temp.GetAccessibilityCode() == temp.Get_Libraries_Interface_Item__CHECKBOX_()){
                                ((Checkbox_)temp).Focus();
                                if (((Checkbox_)temp).GetToggleState())
                                    ((Checkbox_)temp).SetToggleState(false);
                                else
                                    ((Checkbox_)temp).SetToggleState(true);
                            } else if(temp.GetAccessibilityCode() == temp.Get_Libraries_Interface_Item__RADIO_BUTTON_()){
                                ((RadioButton_)temp).Focus();
                                ((RadioButton_)temp).SetToggleState(true);
                            } else if(temp.GetAccessibilityCode() == temp.Get_Libraries_Interface_Item__TAB_()){
                                ((Tab_)temp).Focus();
                                ((Tab_)temp).ClickedMouse();
                                ((Tab_)temp).SetToggleState(true);
                            }
                        }

                        scaleFactor = 1.0f;
                        eventArray = new TouchEvent_[1];
                        event = new TouchEvent();
                        id = e.getPointerId(e.getActionIndex());
                        x = (int)e.getRawX();
                        y = GameStateManager.display.GetHeight() - (int)e.getRawY();
                        maxFingerCount = e.getPointerCount();
                        currentFingerCount = e.getPointerCount();

                        event.x = x;
                        event.y = y;
                        event.fingerID = id;
                        event.eventType = event.BEGAN;
                      	map.put(id, new Coordinates(x, y));
                        event.movementX = 0;
                        event.movementY = 0;
                        eventArray[0] = event;
                        break;
                    case (MotionEvent.ACTION_MOVE):
                    eventArray = new TouchEvent_[e.getPointerCount()];

                    for (int i = 0; i < e.getPointerCount(); i++)
                    {
                        event = new TouchEvent();

                        id = e.getPointerId(i);

                        x = (int)e.getRawX();
                        y = GameStateManager.display.GetHeight() - (int)e.getRawY();

                        currentFingerCount = e.getPointerCount();
                        event.x = x;
                        event.y = y;
                        event.fingerID = id;
                        event.eventType = event.MOVED;
                        coordinates = map.get(id);

                        if (coordinates != null) {
                            event.movementX = event.x - coordinates.x;
                            event.movementY = event.y - coordinates.y;
                            coordinates.x = x;
                            coordinates.y = y;
                        } else {
                            event.movementX = 0;
                            event.movementY = 0;
                        }
                        eventArray[i] = event;
                    }
                    break;
                    case (MotionEvent.ACTION_UP):
                        currentFingerCount = e.getPointerCount();
                    case (MotionEvent.ACTION_POINTER_UP):
                        eventArray = new TouchEvent_[1];
                        event = new TouchEvent();
                        id = e.getPointerId(e.getActionIndex());
                        currentFingerCount = e.getPointerCount();
                        x = (int)e.getRawX();
                        y = GameStateManager.display.GetHeight() - (int)e.getRawY();
                        event.fingerID = id;
                        event.eventType = event.ENDED;
                        coordinates = map.get(id);
                        event.movementX = x - coordinates.x;
                        event.movementY = y - coordinates.y;
                        event.x = x;
                        event.y = y;
                        map.remove(id);
                        eventArray[0] = event;
                        break;
                    case (MotionEvent.ACTION_CANCEL):
                        eventArray = new TouchEvent_[e.getPointerCount()];
                    for (int i = 0; i < e.getPointerCount(); i++)
                    {
                        event = new TouchEvent();

                        id = e.getPointerId(i);

                        x = (int)e.getRawX();
                        y = GameStateManager.display.GetHeight() - (int)e.getRawY();
                        event.fingerID = id;

                        event.x = x;
                        event.y = y;
                        event.fingerID = id;
                        event.eventType = event.CANCELLED;
                        coordinates = map.get(id);
                        event.movementX = x - coordinates.x;
                        event.movementY = y - coordinates.y;
                        map.remove(id);
                        eventArray[i] = event;
                    }
                        break;
                    case (MotionEvent.ACTION_OUTSIDE):
                    default:
                        id = e.getPointerId(e.getActionIndex());
                        eventArray = new TouchEvent_[1];
                        event = new TouchEvent();
                        x = (int)e.getRawX();
                        y = GameStateManager.display.GetHeight() - (int)e.getRawY();
                        event.fingerID = id;
                        for(int i = 0; i < e.getPointerCount(); i++)
                        {
                        if (!map.containsKey(e.getPointerId(i)))
                            map.put(e.getPointerId(i), new Coordinates(x, y));
                        }
                        event.eventType = -1;
                        eventArray[0] = event;
            }
        
        return eventArray;
    }
    
    public List_ GetEvents()
    {
        return touchEvents;
    }

    public void AddGestureEvent(GestureEvent_ event)
    {
        gestureEvents.Add(event);
    }

    private void InitializeValues(MotionEvent event, GestureEvent quorumEvent)
    {
        int x = 0;
        int y = 0;
        if (event != null){
            x = (int)event.getRawX();
            y = GameStateManager.display.GetHeight() - (int)event.getRawY();
        }


        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__x_(x);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__y_(y);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__maxFingerCount_(maxFingerCount);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__currentFingerCount_(currentFingerCount);
    }

    private void InitializeValues(ScaleGestureDetector detector, GestureEvent quorumEvent)
    {
        int x = (int)detector.getFocusX();
        int y = GameStateManager.display.GetHeight() - (int)detector.getFocusY();

        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__x_(x);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__y_(y);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__maxFingerCount_(maxFingerCount);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__currentFingerCount_(currentFingerCount);
    }

    public void AddSingleTapEvent(MotionEvent event)
    {
        GestureEvent quorumEvent = new GestureEvent();
    	//Log.d(DEBUG_TAG, "AndroidInput - TAP");
    	//Log.d("myapp", Log.getStackTraceString(new Exception()));
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__eventType_(quorumEvent.SINGLE_TAP);

        InitializeValues(event, quorumEvent);

        AddGestureEvent(quorumEvent);
    }

    public void AddDoubleTapEvent(MotionEvent event)
    {
        GestureEvent quorumEvent = new GestureEvent();
    	//Log.d(DEBUG_TAG, "AndroidInput - DOUBLE TAP");
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__eventType_(quorumEvent.DOUBLE_TAP);

        InitializeValues(event, quorumEvent);

        AddGestureEvent(quorumEvent);
    }

    public void AddFlingEvent(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY)
    {
        GestureEvent quorumEvent = new GestureEvent();
    	//Log.d(DEBUG_TAG, "AndroidInput - SWIPE");
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__eventType_(quorumEvent.SWIPE);

        if (Math.abs(velocityX) > Math.abs(velocityY))
        {
            if (velocityX > 0)
                quorumEvent.Set_Libraries_Interface_Events_GestureEvent__direction_(quorumEvent.RIGHT);
            else
                quorumEvent.Set_Libraries_Interface_Events_GestureEvent__direction_(quorumEvent.LEFT);
        }
        else
        {
            // The provided Y distance is in Android's coordinate space, which is reversed from ours. So we flip the signs here.
            if (velocityY < 0)
                quorumEvent.Set_Libraries_Interface_Events_GestureEvent__direction_(quorumEvent.UP);
            else
                quorumEvent.Set_Libraries_Interface_Events_GestureEvent__direction_(quorumEvent.DOWN);
        }

        InitializeValues(event1, quorumEvent);

        AddGestureEvent(quorumEvent);
    }

    public void AddLongPressEvent(MotionEvent event)
    {
        GestureEvent quorumEvent = new GestureEvent();
    	//Log.d(DEBUG_TAG, "AndroidInput - LONG PRESS");
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__eventType_(quorumEvent.LONG_PRESS);

        InitializeValues(event, quorumEvent);

        AddGestureEvent(quorumEvent);

        longPressed = true;
    }

    public void TestLongPressEnd(MotionEvent event)
    {
        if (longPressed == false)
            return;

        longPressed = false;

        GestureEvent quorumEvent = new GestureEvent();
    	//Log.d(DEBUG_TAG, "AndroidInput - LONG PRESS (FINISH)");
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__eventType_(quorumEvent.LONG_PRESS);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__timingCode_(quorumEvent.FINISH);

        InitializeValues(event, quorumEvent);

        AddGestureEvent(quorumEvent);
    }

    public void AddScrollEvent(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY)
    {
        GestureEvent quorumEvent = new GestureEvent();
    	//Log.d(DEBUG_TAG, "AndroidInput - PAN");
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__eventType_(quorumEvent.PAN);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__panDistanceX_((int)-distanceX);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__panDistanceY_((int)distanceY);

        if (Math.abs(distanceX) > Math.abs(distanceY))
        {
            if (distanceX > 0)
                quorumEvent.Set_Libraries_Interface_Events_GestureEvent__direction_(quorumEvent.RIGHT);
            else
                quorumEvent.Set_Libraries_Interface_Events_GestureEvent__direction_(quorumEvent.LEFT);
        }
        else
        {
            // The provided Y distance is in Android's coordinate space, which is reversed from ours. So we flip the signs here.
            if (distanceY < 0)
                quorumEvent.Set_Libraries_Interface_Events_GestureEvent__direction_(quorumEvent.UP);
            else
                quorumEvent.Set_Libraries_Interface_Events_GestureEvent__direction_(quorumEvent.DOWN);
        }

        InitializeValues(event1, quorumEvent);

        AddGestureEvent(quorumEvent);
    }

    public void AddScaleBeginEvent(ScaleGestureDetector detector)
    {
        GestureEvent quorumEvent = new GestureEvent();
    	//Log.d(DEBUG_TAG, "AndroidInput - PINCH (BEGIN)");
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__eventType_(quorumEvent.PINCH);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__timingCode_(quorumEvent.BEGIN);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__scaleFactor_(detector.getScaleFactor());

        InitializeValues(detector, quorumEvent);

        AddGestureEvent(quorumEvent);
    }

    public void AddScaleContinueEvent(ScaleGestureDetector detector)
    {
        GestureEvent quorumEvent = new GestureEvent();
    	//Log.d(DEBUG_TAG, "AndroidInput - PINCH (CONTINUE)");
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__eventType_(quorumEvent.PINCH);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__timingCode_(quorumEvent.CONTINUE);

        scaleFactor *= detector.getScaleFactor();
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__scaleFactor_(detector.getScaleFactor());

        InitializeValues(detector, quorumEvent);

        AddGestureEvent(quorumEvent);
    }

    public void AddScaleEndEvent(ScaleGestureDetector detector)
    {
        GestureEvent quorumEvent = new GestureEvent();
    	//Log.d(DEBUG_TAG, "AndroidInput - PINCH (FINISH)");
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__eventType_(quorumEvent.PINCH);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__timingCode_(quorumEvent.FINISH);
        quorumEvent.Set_Libraries_Interface_Events_GestureEvent__scaleFactor_(scaleFactor);
        if (scaleFactor > 1.5 || scaleFactor < 0.5)
            quorumEvent.Set_Libraries_Interface_Events_GestureEvent__isPinch_(true);


        InitializeValues(detector, quorumEvent);

        AddGestureEvent(quorumEvent);
    }

    public List_ GetGestureEvents()
    {
        return gestureEvents;
    }
    
    private class Coordinates
    {
        public int x;
        public int y;
        
        public Coordinates(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }
}
