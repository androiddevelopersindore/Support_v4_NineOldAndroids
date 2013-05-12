#Android Support Library v4 with NineOldAndroids

* Object Animator API's for [Fragment Transitions](#transition)
  * [Standard transitions](#standard)
  * [Custom Transitions](#custom)
  * [Fragment implementation](#fragment)
  * [Style resources](#style)
* [ViewPager PagerTransformers](#pager)

###General Usage

Your project must have [NineOldAndroids](http://nineoldandroids.com) in the classpath.  This can be done with Maven or putting the jar into the /libs folder. This project is packages as an APK Library to support style resources.  Import it into eclipse and reference it as an Android Library. Right-click on project and `Properties->Android`

***

###<a id="transition"></a>Animator Fragment Transitions

This fork allows using [NineOldAndroids](http://nineoldandroids.com) Object Animators for fragment transitions.  View animations will no longer work.

####<a id="standard"></a>Standard Transitions

Specify standard transitions in the transaction.

	tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

####<a id="custom"></a>Custom Transitions

Specify custom transitions in the transaction

    tx.setCustomTransitions(R.animator.flip_left_in, R.animator.flip_left_out, R.animator.flip_right_in, R.animator.flip_right_out)

####<a id="fragment"></a>Fragment Specified Transitions

Specify transition in Fragment implementation

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
       //If transaction specifies a custom animation, use it
       if(nextAnim>0)
          return AnimatorInflater.loadAnimator(getActivity(), nextAnim);
       if(enter)
          return AnimatorInflater.loadAnimator(getActivity(), R.animator.fade_in);
       else
          return AnimatorInflater.loadAnimator(getActivity(), R.animator.fade_out);
    }

####<a id="style"></a>Transition style resources

Specify transitions in a style resource.

Create a style resource `res/values/styles.xml'

    <?xml version="1.0" encoding="utf-8"?>
    <resources>
    	<!-- Specify Transitions with a Style resource -->
    	<style name="MyTransitionStyle">
		    <item name="fragmentFadeEnterAnimation">@animator/fade_enter</item>
		    <item name="fragmentFadeExitAnimation">@animator/fade_exit</item>
		    <item name="fragmentOpenEnterAnimation">@animator/flip_left_in</item>
		    <item name="fragmentOpenExitAnimation">@animator/flip_left_out</item>
		    <item name="fragmentCloseEnterAnimation">@animator/flip_left_in</item>
		    <item name="fragmentCloseExitAnimation">@animator/flip_left_out</item>
    	</style>
    </resources>

Specify the resource in the transaction

    tx.setTransitionStyle(R.style.MyTransitionStyle);

***

###<a id="pager"></a>PageTransformer

ViewPager is modified to support custom PageTransformers implemented with NineOldAndroids.  For example:

```java
public class ZoomOutPageTransformer implements PageTransformer {
    private static float MIN_SCALE = 0.85f;
    private static float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewHelper.setAlpha(view, 0);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                ViewHelper.setTranslationX(view, horzMargin - vertMargin / 2);
            } else {
                ViewHelper.setTranslationX(view, -horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            ViewHelper.setScaleX(view, scaleFactor);
            ViewHelper.setScaleY(view, scaleFactor);

            // Fade the page relative to its size.
            ViewHelper.setAlpha(view, MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewHelper.setAlpha(view, 0);
        }
    }
}
```

Set the PageTransformer as usual:

    viewPager.setPageTransformer(new ZoomOutPageTransformer());
