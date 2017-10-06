package node.com.enjoydanang.framework;

/**
 * Fragment Transition Information. <br/>
 * This usually used in {@code #FragmentTransition.replace()} to replace Fragment.
 * @author thaonp & phatvt
 */
public class FragmentTransitionInfo {
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Animation Resource Id when Fragment will be Enter.
	 */
	public int enterAnimationId = -1;
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Animation Resource Id when Fragment will be Exit.
	 */
	public int exitAnimationId = -1;
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Animation Resource Id when Fragment will be Enter in case use Pop from BackStack.
	 */
	public int popEnterAnimationId = -1;
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Animation Resource Id when Fragment will be Exit in case use Pop from BackStack.
	 */
	public int popExitAnimationId = -1;
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Default constructor.
	 */
	public FragmentTransitionInfo() {
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param enter Animation Resource Id when Fragment will be Enter.
	 * @param exit Animation Resource Id when Fragment will be Exit.
	 * @param popEnter Animation Resource Id when Fragment will be Enter in case use Pop from BackStack.
	 * @param popExit Animation Resource Id when Fragment will be Exit in case use Pop from BackStack.
	 */
	public FragmentTransitionInfo(int enter, int exit, int popEnter, int popExit) {
		enterAnimationId = enter;
		exitAnimationId = exit;
		popEnterAnimationId = popEnter;
		popExitAnimationId = popExit;
	}
	//--------------------------------------------------------------------------------------------------------------------
}
