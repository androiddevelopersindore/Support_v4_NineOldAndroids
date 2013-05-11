#Android Support Library v4 with NineOldAndroids

* Object Animator API's with Fragment Transitions
* ViewPager PagerTransformers

###General Usage

Your project must have [NineOldAndroids](http://nineoldandroids.com) as well as this project in the classpath.  This can be done with Maven or simple putting the jars into the /libs folder.

###Animator Fragment Transitions

This fork allows using [NineOldAndroids](http://nineoldandroids.com) Object Animators for fragment transitions.  View animations will no longer work.

####Standard Transitions

Specify standard transitions in the transaction (OPEN/CLOSE don't work prefectly. Something is wrong with my ObjectAnimators (please help))

	tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

####Custom Transitions

Specify custom transitions in the transaction

    tx.setCustomTransitions(android.R.animator.fade_in, android.R.animator.fade_out)

####Fragment Specified Transitions

Specify transition in Fragment implementation

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
       if(nextAnim>0)
          return AnimatorInflater.loadAnimator(getActivity(), nextAnim);
       if(enter)
          return AnimatorInflater.loadAnimator(getActivity(), android.R.animator.fade_in);
       else
          return AnimatorInflater.loadAnimator(getActivity(), android.R.animator.fade_out);
    }

####Transition style resources

Will be supported in next soon.  Though this will require packaging the project as an APK Library instead of a jar file.

###PageTransformer

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
