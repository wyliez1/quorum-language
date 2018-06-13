#include <windows.h>
#include <UIAutomation.h>

#include "CustomMessages.h"
#include "Item.h"

class CheckBoxProvider;

class CheckBoxControl : public Item
{
public:
	CheckBoxControl(_In_ WCHAR* name, _In_ WCHAR* description);
	virtual ~CheckBoxControl();

	static CheckBoxControl* Create(_In_ HINSTANCE instance, _In_ WCHAR* buttonName, _In_ WCHAR* buttonDescription);

	CheckBoxProvider* GetButtonProvider(_In_ HWND hwnd);
	
	void InvokeButton(_In_ HWND hwnd);

	void SetState(_In_ ToggleState controlState);
	ToggleState GetState();

	bool HasFocus();

private:
	static LRESULT CALLBACK StaticToggleButtonControlWndProc(_In_ HWND hwnd, _In_ UINT message, _In_ WPARAM wParam, _In_ LPARAM lParam);
	LRESULT CALLBACK ToggleButtonControlWndProc(_In_ HWND hwnd, _In_ UINT message, _In_ WPARAM wParam, _In_ LPARAM lParam);

	static bool Initialize(_In_ HINSTANCE hInstance);
	static bool Initialized;

	void SetControlFocus(_In_ bool focused);

	bool m_focused;

	ToggleState m_toggleState;
	CheckBoxProvider* m_buttonProvider;

};