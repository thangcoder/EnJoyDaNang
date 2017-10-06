package node.com.enjoydanang.framework;

import node.com.enjoydanang.LogApp;
import node.com.enjoydanang.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Fragment Utility
 * @author thaonp & phatvt
 */
public class FragmentHelper {
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Get class tag
	 * @return Full quality class name
	 */
	public static String classTag() {
		return FragmentHelper.class.getName();
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Set custom animations to transaction
	 * @param transaction transaction needs to set animations
	 * @param transition transition animations
	 */
	public static void setCustomAnimations(final FragmentTransaction transaction, FragmentTransitionInfo transition) {
		if(transition != null) {
			if(transition.enterAnimationId != -1 && transition.exitAnimationId != -1) {
				if(transition.popEnterAnimationId != -1 && transition.popExitAnimationId != -1) {	//Apply to Pop BackStack
					transaction.setCustomAnimations(transition.enterAnimationId, transition.exitAnimationId, transition.popEnterAnimationId, transition.popExitAnimationId);
				}
				else {	//Not Apply to Pop BackStack
					transaction.setCustomAnimations(transition.enterAnimationId, transition.exitAnimationId);
				}
			}
		}
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Add fragment to Fragment Activity
	 * @param fragmentActivity Fragment Activity needs to add fragment
	 * @param fragment Fragment will be added
	 * @param viewGroupId Fragment container ID
	 * @param tag Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
	 * @param transition Transition animations
	 * @return <code>true</code> if added successfully, otherwise is <code>false</code>
	 */
	public static boolean addFragment(final FragmentActivity fragmentActivity, final Fragment fragment, int viewGroupId, String tag, FragmentTransitionInfo transition) {
		try {
			FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
			FragmentHelper.setCustomAnimations(transaction, transition);
			transaction.add(viewGroupId, fragment, tag);
			if(tag != null && !tag.isEmpty()) {
				transaction.addToBackStack(tag);
			}
			transaction.commit();
			return true;
		}
		catch(Exception e) {
			LogApp.log(classTag(), e.getMessage());
			return false;
		}
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Add fragment to Fragment Activity by fragment class tag name.
	 * @param fragmentActivity Fragment Activity needs to add fragment
	 * @param viewGroupId Fragment container ID
	 * @param fragmentClassNameTag Full quality fragment class name
	 * @param addToBackStack <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>fragmentClassNameTag</code> tag name
	 * @param data Passed data 
	 * @param transition Transition animations
	 * @return <code>true</code> if added successfully, otherwise is <code>false</code>
	 */
	public static boolean addFragment(final FragmentActivity fragmentActivity, int viewGroupId, String fragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
		Fragment fragment = Fragment.instantiate(fragmentActivity.getApplicationContext(), fragmentClassNameTag);
		if(data != null) {
			fragment.setArguments(data);
		}
		return FragmentHelper.addFragment(fragmentActivity, fragment, viewGroupId, addToBackStack ? fragmentClassNameTag : null, transition);
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Add child fragment to parent fragment
	 * @param parent Parent fragment
	 * @param child Child fragment
	 * @param viewGroupId Fragment container ID
	 * @param tag Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
	 * @param transition Transition animations
	 * @return <code>true</code> if added successfully, otherwise is <code>false</code>
	 */
	public static boolean addChildFragment(final Fragment parent, final Fragment child, int viewGroupId, String tag, FragmentTransitionInfo transition) {
		try {
			FragmentTransaction transaction = parent.getChildFragmentManager().beginTransaction();
			FragmentHelper.setCustomAnimations(transaction, transition);
			transaction.add(viewGroupId, child, tag);
			if(tag != null && !tag.isEmpty()) {
				transaction.addToBackStack(tag);
			}
			transaction.commit();
			return true;
		}
		catch(Exception e) {
			LogApp.log(classTag(), e.getMessage());
			return false;
		}
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Add child fragment to parent fragment by child fragment class tag name
	 * @param parent Parent fragment
	 * @param viewGroupId Fragment container ID
	 * @param childFragmentClassNameTag Full quality child fragment class name
	 * @param addToBackStack <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>childFragmentClassNameTag</code> tag name
	 * @param data Passed data 
	 * @param transition Transition animations
	 * @return <code>true</code> if added successfully, otherwise is <code>false</code>
	 */
	public static boolean addChildFragment(final Fragment parent, int viewGroupId, String childFragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
		Fragment child = Fragment.instantiate(parent.getActivity().getApplicationContext(), childFragmentClassNameTag);
		if(data != null) {
			child.setArguments(data);
		}
		return FragmentHelper.addChildFragment(parent, child, viewGroupId, addToBackStack ? childFragmentClassNameTag : null, transition);
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Replace fragment from Fragment Activity of specific ViewGroup ID
	 * @param fragmentActivity Fragment Activity needs to replace fragment
	 * @param fragment Fragment will be replaced
	 * @param viewGroupId Fragment container ID
	 * @param tag Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
	 * @param transition Transition animations
	 * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
	 */
	public static boolean replaceFragment(final FragmentActivity fragmentActivity, final Fragment fragment, int viewGroupId, final String tag, FragmentTransitionInfo transition) {
		try {
			FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
			FragmentHelper.setCustomAnimations(transaction, transition);
			transaction.replace(viewGroupId, fragment, tag);
			if(tag != null && !tag.isEmpty()) {
				transaction.addToBackStack(tag);
			}
			transaction.commit();
			return true;
		}
		catch(Exception e) {
			LogApp.log(classTag(), e.getMessage());
		}
		return false;
	}
	//-------------------------------------------------------------------------------------------------------------------
	//
	// ! Favourites Functions
	//

	/**
	 * Replace fragment from Fragment Activity of specific ViewGroup ID by fragment class tag name.
	 * @param fragmentActivity Fragment Activity needs to replace fragment
	 * @param viewGroupId Fragment container ID
	 * @param fragmentClassNameTag Full quality fragment class name
	 * @param addToBackStack <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>fragmentClassNameTag</code> tag name
	 * @param data Passed data 
	 * @param transition Transition animations
	 * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
	 */
	public static boolean replaceFragment(final FragmentActivity fragmentActivity, int viewGroupId,
                                          String fragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
		Fragment fragment = Fragment.instantiate(fragmentActivity.getApplicationContext(), fragmentClassNameTag);
		if(data != null) {
			fragment.setArguments(data);
		}
		return FragmentHelper.replaceFragment(fragmentActivity, fragment, viewGroupId, addToBackStack ? fragmentClassNameTag : null, transition);
	}
	public static boolean replaceFragment(final FragmentActivity fragmentActivity, int viewGroupId, Fragment fragment, String fragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
		if(data != null) {
			fragment.setArguments(data);
		}
		return FragmentHelper.replaceFragment(fragmentActivity, fragment, viewGroupId, addToBackStack ? fragmentClassNameTag : null, transition);
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Replace fragment from parent fragment of specific ViewGroup ID
	 * @param parent Parent fragment
	 * @param child Child fragment
	 * @param viewGroupId Fragment container ID
	 * @param tag Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
	 * @param transition Transition animations
	 * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
	 */
	public static boolean replaceChildFragment(final Fragment parent, final Fragment child, int viewGroupId, String tag, FragmentTransitionInfo transition) {
		try {
			FragmentTransaction transaction = parent.getChildFragmentManager().beginTransaction();
			FragmentHelper.setCustomAnimations(transaction, transition);
			transaction.replace(viewGroupId, child, tag);
			if(tag != null && !tag.isEmpty()) {
				transaction.addToBackStack(tag);
			}
			transaction.commit();
			return true;
		}
		catch(Exception e) {
			LogApp.log(classTag(), e.getMessage());
			return false;
		}
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Replace fragment from parent fragment of specific ViewGroup ID by child fragment class tag name
	 * @param parent Parent fragment
	 * @param viewGroupId Fragment container ID
	 * @param childFragmentClassNameTag Full quality child fragment class name
	 * @param addToBackStack <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>childFragmentClassNameTag</code> tag name
	 * @param data Passed data 
	 * @param transition Transition animations
	 * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
	 */
	public static boolean replaceChildFragment(final Fragment parent, int viewGroupId, String childFragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
		Fragment child = Fragment.instantiate(parent.getActivity().getApplicationContext(), childFragmentClassNameTag);
		if(data != null) {
			child.setArguments(data);
		}
		return FragmentHelper.replaceChildFragment(parent, child, viewGroupId, addToBackStack ? childFragmentClassNameTag : null, transition);
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Find fragment from Fragment Activity by tab name.
	 * @param fragmentActivity Fragment Activity needs to find fragment
	 * @param tag Tag of fragment when add or replace by transaction
	 * @return Found <code>Fragment</code> or <code>null</code> if not found
	 */
	public static Fragment findFragmentByTag(final FragmentActivity fragmentActivity, final String tag) {
		try {
			return fragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
		}
		catch(Exception e) {
			LogApp.log(classTag(), e.getMessage());
			return null;
		}
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Find child fragment from parent fragment by tab name.
	 * @param parent Parent Fragment needs to find child fragment
	 * @param tag Tag of child fragment when add or replace to parent fragment by transaction
	 * @return Found <code>Fragment</code> or <code>null</code> if not found
	 */
	public static Fragment findChildFragmentByTag(final Fragment parent, final String tag) {
		try {
			return parent.getChildFragmentManager().findFragmentByTag(tag);
		}
		catch(Exception e) {
			LogApp.log(classTag(), e.getMessage());
			return null;
		}
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Clear fragment back stack
	 * @param fragmentActivity
	 */
	public static void clearBackStack(final FragmentActivity fragmentActivity) {
		fragmentActivity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public static void clearBackStackImmediate(final FragmentActivity fragmentActivity) {
			fragmentActivity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Clear children fragment back stack
	 * @param parent Parent Fragment
	 */
	public static void clearChildBackStack(final Fragment parent) {
		parent.getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Clear fragment back stack by Iterator
	 * @param fragmentActivity
	 */
	public static void clearBackStackByIterator(final FragmentActivity fragmentActivity) {
		FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
		int backStackCount = fragmentManager.getBackStackEntryCount();
		for (int i = 0; i < backStackCount; i++) {
		    int backStackId = fragmentManager.getBackStackEntryAt(i).getId(); // Get the back stack fragment id.
		    fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Clear children fragment back stack by Iterator
	 * @param parent Parent Fragment
	 */
	public static void clearChildrenBackStackByIterator(final Fragment parent) {
		FragmentManager fragmentManager = parent.getChildFragmentManager();
		int backStackCount = fragmentManager.getBackStackEntryCount();
		for (int i = 0; i < backStackCount; i++) {
			int backStackId = fragmentManager.getBackStackEntryAt(i).getId(); // Get the back stack fragment id.
			fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Pop back stack fragment
	 * @param fragmentActivity
	 */
	public static void popBackStack(final FragmentActivity fragmentActivity) {
		fragmentActivity.getSupportFragmentManager().popBackStack();
	}
	//-------------------------------------------------------------------------------------------------------------------
	public static void popByTag(FragmentActivity activity, String tag){
		FragmentManager manager = activity.getSupportFragmentManager();
		manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Pop back stack fragment
	 * @param parent Parent Fragment
	 */
	public static void popBackStack(final Fragment parent) {
		parent.getChildFragmentManager().popBackStack();
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Get Left to Right Fragment transition information.
	 * @return
	 */
	public static FragmentTransitionInfo getLeftToRightTransition() {
		FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo();
		transitionInfo.enterAnimationId = R.anim.fragment_enter_left_to_right;
		transitionInfo.exitAnimationId = R.anim.fragment_exit_left_to_right;
		transitionInfo.popEnterAnimationId = R.anim.fragment_enter_right_to_left;
		transitionInfo.popExitAnimationId = R.anim.fragment_exit_right_to_left;
		return transitionInfo;
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Get Right to Left Fragment transition information.
	 * @return
	 */
	public static FragmentTransitionInfo getRightToLeftTransition() {
		FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo();
		transitionInfo.enterAnimationId = R.anim.fragment_enter_right_to_left;
		transitionInfo.exitAnimationId = R.anim.fragment_exit_right_to_left;
		transitionInfo.popEnterAnimationId = R.anim.fragment_enter_left_to_right;
		transitionInfo.popExitAnimationId = R.anim.fragment_exit_left_to_right;
		return transitionInfo;
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Get bottom to top fragment transition information without enter enimation.
	 * @return
	 */
	public static FragmentTransitionInfo getBottomToTopTransition() {
		FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo();
		transitionInfo.enterAnimationId = R.anim.fragment_enter_bottom_up;
		transitionInfo.exitAnimationId = R.anim.fragment_standing;
		transitionInfo.popEnterAnimationId = R.anim.fragment_standing;
		transitionInfo.popExitAnimationId = R.anim.fragment_exit_top_down;
		return transitionInfo;
	}
	//--------------------------------------------------------------------------------------------------------------------
}
