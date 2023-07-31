package plugins.quorum.Libraries.Interface.Accessibility;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.*;
import org.robovm.apple.uikit.*;
import plugins.quorum.Libraries.Game.IOSApplication;
import plugins.quorum.Libraries.Game.IOSDelegate;
import quorum.Libraries.Game.Shapes.Rectangle_;
import quorum.Libraries.Interface.Controls.Button_;
import quorum.Libraries.Interface.Controls.TextField_;
import quorum.Libraries.Interface.Controls.ToggleButton_;
import quorum.Libraries.Interface.Events.*;
import quorum.Libraries.Interface.Item2D_;
import quorum.Libraries.Interface.Item3D_;
import quorum.Libraries.Interface.Item_;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IOSAccessibility {
    public Object me_ = null;
    public HashMap mapAccessibilityElements = new HashMap<UIAccessibilityElement, Item_>();

    public void  NameChanged(Item_ item) {}

    public void  DescriptionChanged(Item_ item) {}

    public void  BoundsChanged(Item_ item) {}

    public void  TextFieldUpdatePassword(TextField_ field) {}

    public void  Update() {
        // Update the accessibility elements
//        Iterator it = mapAccessibilityElements.entrySet().iterator();
//        int screenHeight =  (int) IOSDelegate.app.GetUIViewController().getView().getBounds().getHeight();
//
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            UIAccessibilityElement element = (UIAccessibilityElement) pair.getKey();
//            Item_ item = (Item_) pair.getValue();
//            int x = 0;
//            int y = 0;
//            int width = 0;
//            int height = 0;
//
//            if (item instanceof Item2D_)
//            {
//                double itemX = ((Item2D_)item).GetScreenX();
//                double itemY = (((Item2D_) item).GetScreenY());
//
//                if (itemX == Double.NaN)
//                    itemX = 0;
//
//                if (itemY == Double.NaN)
//                    itemY = 0;
//                width = (int) (((Item2D_) item).GetWidth() / IOSApplication.containerScaleFactorWidth);
//                height = (int) (((Item2D_) item).GetHeight() / IOSApplication.containerScaleFactorHeight);
//                x = (int)(itemX / IOSApplication.containerScaleFactorWidth);
//                y = (int)(IOSApplication.accessibilityContainerBounds.getHeight() - ( height + (itemY / IOSApplication.containerScaleFactorHeight)));
//            }
//            else if (item instanceof Item3D_)
//            {
//                // This is only a place holder, to place a small box roughly at the
//                // center of a 3D object in the screen. To calculate this correctly,
//                // check how we calculate mouse input detection for 3D objects.
//
//                Rectangle_ rectangle = ((Item3D_) item).GetScreenBounds();
//
//                width = (int) (rectangle.GetWidth() / IOSApplication.containerScaleFactorWidth);
//                height = (int)(rectangle.GetY() + rectangle.GetHeight() / IOSApplication.containerScaleFactorHeight);
//                x = (int)(rectangle.GetX() / IOSApplication.containerScaleFactorWidth);
//                y = (int)(IOSApplication.accessibilityContainerBounds.getHeight() - ( height + (rectangle.GetY() / IOSApplication.containerScaleFactorHeight)));
//            }
//            else
//            {
//                return;
//            }
//            CGRect rect = new CGRect(x, y, width, height);
//            if (element.getAccessibilityFrame().equals(rect))
//                continue;
//            element.setAccessibilityFrame(rect);
//            System.out.println("Setting accessibility element" + rect.getX() + " " + rect.getY() + " " + rect.getWidth() + " " + rect.getHeight());
//            System.out.println("Setting accessibility element" + element.getAccessibilityFrame().getX() + " "
//                            + element.getAccessibilityFrame().getY() + " " + element.getAccessibilityFrame().getWidth()
//                            + " " + element.getAccessibilityFrame().getHeight());
//
//            System.out.println("Setting accessibility element" + " " + item.GetName() + " " + item.GetDescription());
//
//            // Inform iOS that the accessibility elements have changed
//            UIAccessibilityGlobals.postNotification(UIAccessibilityNotification.LayoutChangedNotification, element);
//        }

    }

    public void  ProgressBarValueChanged(ProgressBarValueChangedEvent_ progress) {}

    public void  SelectionChanged(SelectionEvent_ event) {}

    public void  ButtonActivated(Button_ button) {}

    public void  ToggleButtonToggled(ToggleButton_ button) {}

    public void  FocusChanged(FocusEvent_ event) throws Exception {
        Item_ item = event.GetNewFocus();
        System.out.println("Focus Changed to " + item.GetName());
        UIAccessibilityElement element = (UIAccessibilityElement) mapAccessibilityElements.get(item);
        UIAccessibilityGlobals.postNotification(UIAccessibilityNotification.ScreenChangedNotification, element);

    }

    public void Add(Item_ item) {
        boolean debug = false;
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if(item == null) {
            return;
        }

        if (item instanceof Item2D_)
        {
            double itemX = ((Item2D_)item).GetScreenX();
            double itemY = (((Item2D_) item).GetScreenY());
            if (itemX == Double.NaN) {
                itemX = 0;
            }

            if (itemY == Double.NaN) {
                itemY = 0;
            }

            width = (int) (((Item2D_) item).GetWidth() / IOSApplication.containerScaleFactorWidth);
            height = (int) (((Item2D_) item).GetHeight() / IOSApplication.containerScaleFactorHeight);
            x = (int)(itemX / IOSApplication.containerScaleFactorWidth);
            y = (int)(IOSApplication.accessibilityContainerBounds.getHeight() - ( height + (itemY / IOSApplication.containerScaleFactorHeight)));
        }
        else if (item instanceof Item3D_)
        {
            // This is only a place holder, to place a small box roughly at the
            // center of a 3D object in the screen. To calculate this correctly,
            // check how we calculate mouse input detection for 3D objects.
            Rectangle_ rectangle = ((Item3D_) item).GetScreenBounds();

            width = (int) (rectangle.GetWidth() / IOSApplication.containerScaleFactorWidth);
            height = (int)(rectangle.GetY() + rectangle.GetHeight() / IOSApplication.containerScaleFactorHeight);
            x = (int)(rectangle.GetX() / IOSApplication.containerScaleFactorWidth);
            y = (int)(IOSApplication.accessibilityContainerBounds.getHeight() - ( height + (rectangle.GetY() / IOSApplication.containerScaleFactorHeight)));
        }
        else
        {
            return;
        }

        if(debug) {
            System.out.println("Adding accessibility element" + x + " " + y + " " + width + " " + height);
            System.out.println("Adding accessibility element" + " " + item.GetName() + " " + item.GetDescription());

            if (item instanceof Item2D_)
            {
                System.out.println("Adding accessibility element 2d" + " " + ((Item2D_) item).GetScreenX() + " " + ((Item2D_) item).GetScreenY() + " " + ((Item2D_) item).GetWidth() + " " + ((Item2D_) item).GetHeight());
            }
        }

        //Get the accessibility code and do custom controls.
        int code = item.GetAccessibilityCode();
        if (code == item.Get_Libraries_Interface_Item__NOT_ACCESSIBLE_() || !item.IsShowing()) {
            return;
        }





        UIAccessibilityElement accessibilityElement = new UIAccessibilityElement(IOSApplication.accessibilityContainer);

        if (item.GetName() != null) {
            accessibilityElement.setAccessibilityLabel(item.GetName());
        }
        else {
            accessibilityElement.setAccessibilityLabel("Name");
        }

        if (item.GetDescription() != null) {
            accessibilityElement.setAccessibilityHint(item.GetDescription());
        }
        else {
            accessibilityElement.setAccessibilityHint("Description");
        }


        accessibilityElement.setAccessibilityElement(true);
        accessibilityElement.setAccessibilityValue(item.GetDescription());
        accessibilityElement.setAccessibilityIdentifier(item.GetAccessibilityType());
        accessibilityElement.setAccessibilityFrame(new CGRect(x, y, width, height));

        UIAccessibilityTraits traits = UIAccessibilityTraits.AllowsDirectInteraction;
        accessibilityElement.setAccessibilityTraits(traits);

        // Add the accessibility element to the list
        NSMutableArray<UIAccessibilityElement> nsArray = (NSMutableArray<UIAccessibilityElement>) IOSApplication.accessibilityContainer.getAccessibilityElements().mutableCopy();
        nsArray.add(accessibilityElement);
        mapAccessibilityElements.put(accessibilityElement, item);
        IOSApplication.accessibilityContainer.setAccessibilityElements(nsArray);

        // Inform iOS that the accessibility elements have changed
        UIAccessibilityGlobals.postNotification(UIAccessibilityNotification.ScreenChangedNotification, accessibilityElement);

        if(debug){
            System.out.println("The bounds are" + IOSApplication.accessibilityContainer.getFrame().getX() + " " + IOSApplication.accessibilityContainer.getFrame().getY() + " " + IOSApplication.accessibilityContainer.getFrame().getWidth() + " " + IOSApplication.accessibilityContainer.getFrame().getHeight());
        }
    }

    private class HiddenView extends UIView {
        public HiddenView() {

        }
    }

    private class HiddenButton extends UIButton {
        public HiddenButton() {

        }
    }
    private class HiddenTextField extends UITextField {
        public HiddenTextField () {

            setKeyboardType(UIKeyboardType.Default);
            setReturnKeyType(UIReturnKeyType.Done);
            setAutocapitalizationType(UITextAutocapitalizationType.None);
            setAutocorrectionType(UITextAutocorrectionType.No);
            setSpellCheckingType(UITextSpellCheckingType.No);
            setHidden(true);
        }

        @Override
        public void deleteBackward () {
            //app.input.inputProcessor.keyTyped((char)8);
            //super.deleteBackward();
            //Gdx.graphics.requestRendering();
        }
    }

    public void  Remove(Item_ item) {
    }

    public void  MenuChanged(MenuChangeEvent_ event) {}

    public void  TreeChanged(TreeChangeEvent_ event) {}

    public void  TreeTableChanged(TreeTableChangeEvent_ event) {}

    public void  ControlActivated(ControlActivationEvent_ event) {}

    public void  TextChanged(TextChangeEvent_ event) {}

    public void  WindowFocusChanged(WindowFocusEvent_ event) {}

    public void  Notify(Item_ item, String value) {}

    public void  Notify(Item_ item, String value, int notificationType) {}

    public void  Shutdown() {}
}
