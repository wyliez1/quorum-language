#include <UIAutomation.h>

#include "TextBoxProvider.h"
#include "TextBoxControl.h"
#include "TextBoxTextRange.h"

#include <iostream>

void NotifyCaretPositionChanged(_In_ HWND hwnd, _In_ TextBoxControl *control)
{
	//std::cout << "NotifyCaretPositionChanged" << std::endl;
	TextBoxProvider *eventControl = new TextBoxProvider(hwnd, control);
	if (eventControl == NULL)
	{
		// This is an error. Should probably be dealt with somehow.
	}
	else
	{
		UiaRaiseAutomationEvent(eventControl, UIA_Text_TextSelectionChangedEventId);
		UiaRaiseAutomationEvent(eventControl, UIA_AutomationFocusChangedEventId);
		eventControl->Release();
	}
}

void NotifyFocusGained(_In_ HWND hwnd, _In_ TextBoxControl *control)
{
	TextBoxProvider *eventControl = new TextBoxProvider(hwnd, control);
	if (eventControl == NULL)
	{
		// This is an error. Should probably be dealt with somehow.
	}
	else
	{
		UiaRaiseAutomationEvent(eventControl, UIA_AutomationFocusChangedEventId);
		eventControl->Release();
	}
}

TextBoxProvider::TextBoxProvider(_In_ HWND hwnd, _In_ TextBoxControl *control) : m_refCount(1), m_TextBoxControlHWND(hwnd), m_pTextBoxControl(control)
{
	// Nothing to do here.
	EndPoint caret = m_pTextBoxControl->GetCaretPosition();
	std::cout << "Native caret.Line: " << caret.line << std::endl << "Native caret.Character: " << caret.character << std::endl;
}

TextBoxProvider::~TextBoxProvider()
{
}

// =========== IUnknown implementation.

IFACEMETHODIMP_(ULONG) TextBoxProvider::AddRef()
{
	return InterlockedIncrement(&m_refCount);
}

IFACEMETHODIMP_(ULONG) TextBoxProvider::Release()
{
	long val = InterlockedDecrement(&m_refCount);
	if (val == 0)
	{
		delete this;
	}
	return val;
}

IFACEMETHODIMP TextBoxProvider::QueryInterface(_In_ REFIID riid, _Outptr_ void ** ppInterface)
{
	if (riid == __uuidof(IUnknown))
	{
		*ppInterface = static_cast<IRawElementProviderSimple*>(this);
	}
	else if (riid == __uuidof(IRawElementProviderSimple))
	{
		*ppInterface = static_cast<IRawElementProviderSimple*>(this);
	}
	else if (riid == __uuidof(ITextProvider))
	{
		*ppInterface = static_cast<ITextProvider*>(this);
	}
	else
	{
		*ppInterface = NULL;
		return E_NOINTERFACE;
	}

	(static_cast<IUnknown*>(*ppInterface))->AddRef();
	return S_OK;
}

// =========== IRawElementProviderSimple implementation

IFACEMETHODIMP TextBoxProvider::get_ProviderOptions(_Out_ ProviderOptions *pRetVal)
{
	*pRetVal = ProviderOptions_ServerSideProvider | ProviderOptions_UseComThreading;
	return S_OK;
}

IFACEMETHODIMP TextBoxProvider::GetPatternProvider(PATTERNID patternId, _Outptr_result_maybenull_ IUnknown ** pRetVal)
{
	*pRetVal = NULL;

	/*if (patternId == UIA_TextPatternId)
	{
		*pRetVal = static_cast<ITextProvider *>(this);
		(*pRetVal)->AddRef();
	}*/

	return S_OK;
}

IFACEMETHODIMP TextBoxProvider::GetPropertyValue(PROPERTYID propertyId, _Out_ VARIANT * pRetVal)
{
	
	if (propertyId == UIA_LocalizedControlTypePropertyId)
	{
		pRetVal->vt = VT_BSTR;
		pRetVal->bstrVal = SysAllocString(L"Text Box");
	}
	else if (propertyId == UIA_AutomationIdPropertyId)
	{
		pRetVal->bstrVal = SysAllocString(L"Text Box");
		if (pRetVal->bstrVal != NULL)
		{
			pRetVal->vt = VT_BSTR;
		}
	}
	else if (propertyId == UIA_HelpTextPropertyId)
	{
		pRetVal->vt = VT_BSTR;
		pRetVal->bstrVal = SysAllocString(L" What do you want from me?");
	}
	else if (propertyId == UIA_NamePropertyId)
	{
		pRetVal->vt = VT_BSTR;
		pRetVal->bstrVal = SysAllocString(m_pTextBoxControl->GetName());
	}
	else if (propertyId == UIA_ControlTypePropertyId)
	{
		pRetVal->vt = VT_I4;
		pRetVal->lVal = UIA_EditControlTypeId;
		//pRetVal->lVal = UIA_DocumentControlTypeId;
	}
	else if (propertyId == UIA_IsEnabledPropertyId)
	{
		// This tells the screen reader whether or not the control can be interacted with.
		// Hardcoded to true but this property could be dynamic depending on the needs of the Quorum GUI.
		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_TRUE;
	}
	else if (propertyId == UIA_IsKeyboardFocusablePropertyId)
	{
		// Tells the screen reader that this control is capable of getting keyboard focus.
		// This isn't enough for the screen reader to announce the control's existence to the user when it gains focus in Quorum.
		// UIA_HasKeyboardFocusPropertyId is responsible for whether or not the screen reader announces that this control gained focus.
		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_TRUE;
	}
	else if (propertyId == UIA_HasKeyboardFocusPropertyId)
	{
		// This tells the screen reader whether or not this control has Keyboard focus. Normally, only one control/window is allowed to have keyboard focus at a time
		// but by lying and having every instance of this control report that it has keyboard focus then we don't have to mantain what has focus on the native level.
		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_TRUE;
	}
	else if (propertyId == UIA_IsPasswordPropertyId)
	{
		/*
			Identifies the IsPassword property, which is a Boolean value that indicates whether 
			the automation element contains protected content or a password.

			When the IsPassword property is TRUE and the element has the keyboard focus, a client 
			application should disable keyboard echoing or keyboard input feedback that may expose 
			the user's protected information. Attempting to access the Value property of the protected 
			element (edit control) may cause an error to occur.

			For now, this will be hardcoded as FALSE but in the future this can be maintained in Quorum and
			updated down here when textboxes contain protected info.
		*/

		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_FALSE;
	}
	//else if (propertyId == UIA_LabeledByPropertyId)
	//{
		/*
			If there is a static text label associated with this control, then this property must expose 
			a reference to this control. If the TextBox is a subcomponent of another control,
			it will not have a LabeledBy property set. E.g., it's contained within a tab.
		*/

	//}
	else
	{
		pRetVal->vt = VT_EMPTY;
		// UI Automation will attempt to get the property from the host window provider.
		// If the property is found then it will have the UI Automation defaults listed in the Microsoft Developer's Network documentation.
		// More often than not the default values are responsible for a control not functioning properly with a screen reader.
	}

	return S_OK;
}

IFACEMETHODIMP TextBoxProvider::get_HostRawElementProvider(_Outptr_result_maybenull_ IRawElementProviderSimple ** pRetVal)
{
	return UiaHostProviderFromHwnd(m_TextBoxControlHWND, pRetVal);
}


// =========== ITextProvider implementation

// GetSelection: Retrieves a collection of text ranges that represents the currently selected text in a text-based control.
//				 For this control the selection will either be a single text range or a degenerate text range.
IFACEMETHODIMP TextBoxProvider::GetSelection(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	// For now, selection is hardcoded to be the degenerate text range.
	Range caretRange = { m_pTextBoxControl->GetCaretPosition(), m_pTextBoxControl->GetCaretPosition() };
	ITextRangeProvider *selectionRangeProvider = new TextBoxTextRange(m_TextBoxControlHWND, m_pTextBoxControl, caretRange);
	HRESULT hr = S_OK;
	if (selectionRangeProvider == NULL)
	{
		hr = E_OUTOFMEMORY;
	}
	else
	{
		*pRetVal = SafeArrayCreateVector(VT_UNKNOWN, 0, 1);
		if (*pRetVal == NULL)
		{
			hr = E_OUTOFMEMORY;
		}
		else
		{
			long index = 0;
			hr = SafeArrayPutElement(*pRetVal, &index, selectionRangeProvider);
			if (FAILED(hr))
			{
				SafeArrayDestroy(*pRetVal);
				*pRetVal = NULL;
			}
		}
		selectionRangeProvider->Release();
	}

	return hr;
}

// GetVisibleRanges: Retrieves an array of disjoint text ranges from a text-based control where each text range represents a contiguous span of visible text.
IFACEMETHODIMP TextBoxProvider::GetVisibleRanges(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal)
{
	// Not Implemented yet.
	*pRetVal = NULL;
	return S_OK;
}

// RangeFromChild: Retrieves a text range enclosing a child element such as an image, hyperlink, or other embedded object. 
IFACEMETHODIMP TextBoxProvider::RangeFromChild(_In_opt_ IRawElementProviderSimple * childElement, _Outptr_result_maybenull_ ITextRangeProvider ** pRetVal)
{
	UNREFERENCED_PARAMETER(childElement);
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	// There are no children of this text control
	*pRetVal = NULL;
	return S_OK;
}

// RangeFromPoint: Returns the degenerate (empty) text range nearest to the specified screen coordinates.
IFACEMETHODIMP TextBoxProvider::RangeFromPoint(UiaPoint point, _Outptr_result_maybenull_ ITextRangeProvider ** pRetVal)
{
	// Not Implemented yet.
	*pRetVal = NULL;
	return S_OK;
}

// get_DocumentRange: Retrieves a text range that encloses the main text of a document.
IFACEMETHODIMP TextBoxProvider::get_DocumentRange(_Outptr_result_maybenull_ ITextRangeProvider ** pRetVal)
{
	// Not Implemented yet.
	*pRetVal = NULL;
	return S_OK;
}

// get_SupportedTextSelection: Retrieves a value that specifies the type of text selection that is supported by the control.
IFACEMETHODIMP TextBoxProvider::get_SupportedTextSelection(_Out_ SupportedTextSelection * pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	*pRetVal = SupportedTextSelection_Single;
	return S_OK;
}