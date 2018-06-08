#include <windows.h>
#include <UIAutomation.h>

#include "MenuBarProvider.h"
#include "MenuBarControl.h"
#include "MenuItemControl.h"

MenuBarProvider::MenuBarProvider(HWND MenuBarControlHWND, MenuBarControl * pMenuBarControl) : m_menuBarControl(MenuBarControlHWND), m_pMenuBarControl(pMenuBarControl)
{
}

// =========== IUnknown implementation.

IFACEMETHODIMP_(ULONG) MenuBarProvider::AddRef()
{
	return InterlockedIncrement(&m_refCount);
}

IFACEMETHODIMP_(ULONG) MenuBarProvider::Release()
{
	long val = InterlockedDecrement(&m_refCount);
	if (val == 0)
	{
		delete this;
	}
	return val;
}

IFACEMETHODIMP MenuBarProvider::QueryInterface(_In_ REFIID riid, _Outptr_ void ** ppInterface)
{
	if (riid == __uuidof(IUnknown))
	{
		*ppInterface = static_cast<IRawElementProviderSimple*>(this);
	}
	else if (riid == __uuidof(IRawElementProviderSimple))
	{
		*ppInterface = static_cast<IRawElementProviderSimple*>(this);
	}
	else if (riid == __uuidof(IRawElementProviderFragment))
	{
		*ppInterface = static_cast<IRawElementProviderFragment*>(this);
	}
	else if (riid == __uuidof(IRawElementProviderFragmentRoot))
	{
		*ppInterface = static_cast<IRawElementProviderFragmentRoot*>(this);
	}
	else
	{
		*ppInterface = NULL;
		return E_NOINTERFACE;
	}

	(static_cast<IUnknown*>(*ppInterface))->AddRef();
	return S_OK;
}

// Gets UI Automation provider options.
IFACEMETHODIMP MenuBarProvider::get_ProviderOptions(_Out_ ProviderOptions * pRetVal)
{
	*pRetVal = ProviderOptions_ServerSideProvider;
	return S_OK;
}

// The MenuBar doesn't support any patterns so NULL is correct.
IFACEMETHODIMP MenuBarProvider::GetPatternProvider(PATTERNID patternId, _Outptr_result_maybenull_ IUnknown ** pRetVal)
{
	*pRetVal = NULL;
	return S_OK;
}

// Gets the custom properties for this control.
IFACEMETHODIMP MenuBarProvider::GetPropertyValue(PROPERTYID propertyId, _Out_ VARIANT * pRetVal)
{
	if (propertyId == UIA_AccessKeyPropertyId)
	{
		pRetVal->vt = VT_BSTR;
		pRetVal->bstrVal = SysAllocString(L"ALT");
	}
	else if (propertyId == UIA_ControlTypePropertyId)
	{
		pRetVal->vt = VT_I4;
		pRetVal->lVal = UIA_MenuBarControlTypeId;
	}
	else if (propertyId == UIA_IsContentElementPropertyId)
	{
		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_FALSE;
	}
	else if (propertyId == UIA_IsControlElementPropertyId)
	{
		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_TRUE;
	}
	else if (propertyId == UIA_IsKeyboardFocusablePropertyId)
	{
		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_TRUE;
	}
	else if (propertyId == UIA_NamePropertyId)
	{
		pRetVal->vt = VT_BSTR;
		pRetVal->bstrVal = SysAllocString(this->m_pMenuBarControl->GetName());
	}
	else if (propertyId == UIA_OrientationPropertyId)
	{
		pRetVal->vt = VT_BSTR;
		pRetVal->bstrVal = SysAllocString(this->m_pMenuBarControl->GetName());
	}
	else
	{
		pRetVal->vt = VT_EMPTY;
	}

	return S_OK;
}

IFACEMETHODIMP MenuBarProvider::get_HostRawElementProvider(_Outptr_result_maybenull_ IRawElementProviderSimple ** pRetVal)
{
	if (!IsWindow(m_menuBarControl))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	HRESULT hr = UiaHostProviderFromHwnd(m_menuBarControl, pRetVal);
	return hr;
}

// Enables UI Automation to locate the element in the tree.
// Navigation to the parent is handled by the host window provider.
IFACEMETHODIMP MenuBarProvider::Navigate(NavigateDirection direction, _Outptr_result_maybenull_ IRawElementProviderFragment ** pRetVal)
{
	MenuBarControl* pMenuBarControl = this->m_pMenuBarControl;
	MenuItemControl* pMenuItem = NULL;
	IRawElementProviderFragment* pFragment = NULL;
	MENUITEM_ITERATOR iter;

	switch (direction)
	{
	case NavigateDirection_FirstChild:
		iter = pMenuBarControl->GetMenuItemAt(0);
		pMenuItem = static_cast<MenuItemControl*>(*iter);
		pFragment = (IRawElementProviderFragment*)pMenuItem->GetMenuItemProvider(); // TODO: Check that this cast doesn't cause issues.
		break;
	case NavigateDirection_LastChild:
		iter = pMenuBarControl->GetMenuItemAt(pMenuBarControl->GetCount() - 1);
		pMenuItem = static_cast<MenuItemControl*>(*iter);
		pFragment = (IRawElementProviderFragment*)pMenuItem->GetMenuItemProvider();
		break;
	}
	if (pFragment != NULL)
	{
		pFragment->AddRef();
	}
	*pRetVal = pFragment;
	return S_OK;
}

// UI Automation gets this value from the host window provider, so NULL is correct here.
IFACEMETHODIMP MenuBarProvider::GetRuntimeId(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal)
{
	*pRetVal = NULL;
	return S_OK;
}

IFACEMETHODIMP MenuBarProvider::get_BoundingRectangle(_Out_ UiaRect * pRetVal)
{
	// For now we aren't painting a rectangle for the provider
	// that'd require more info from Quorum.
	pRetVal->left = 0;
	pRetVal->top = 0;
	pRetVal->width = 0;
	pRetVal->height = 0;
	return S_OK;
}

// Retrieves other fragment roots that may be hosted in this one.
IFACEMETHODIMP MenuBarProvider::GetEmbeddedFragmentRoots(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal)
{
	*pRetVal = NULL;
	return S_OK;
}

// Responds to the control receiving focus through a UI Automation request.
// For HWND-based controls, this is handled by the host window provider.
IFACEMETHODIMP MenuBarProvider::SetFocus()
{
	return S_OK;
}

// Retrieves the root element of this fragment.
IFACEMETHODIMP MenuBarProvider::get_FragmentRoot(_Outptr_result_maybenull_ IRawElementProviderFragmentRoot ** pRetVal)
{
	*pRetVal = static_cast<IRawElementProviderFragmentRoot*>(this);
	AddRef();
	return S_OK;
}

// Retrieves the IRawElementProviderFragment interface for the item at the specified 
// point (in client coordinates).
IFACEMETHODIMP MenuBarProvider::ElementProviderFromPoint(double x, double y, _Outptr_result_maybenull_ IRawElementProviderFragment ** pRetVal)
{
	// Not implemented yet.
	*pRetVal = NULL;
	return S_OK;
}

// Retrieves the provider for the list item that is selected when the control gets focus.
IFACEMETHODIMP MenuBarProvider::GetFocus(_Outptr_result_maybenull_ IRawElementProviderFragment ** pRetVal)
{
	*pRetVal = NULL;

	MenuItemControl* pMenuItem = m_pMenuBarControl->GetSelectedMenuItem();
	if (pMenuItem != NULL)
	{
		// TODO: Get Provider

		// If provider isn't null assign to pRetVal.
	}

	return S_OK;
}



MenuBarProvider::~MenuBarProvider()
{
}


