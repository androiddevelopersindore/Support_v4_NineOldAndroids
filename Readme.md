#Android Support Library v4 with NineOldAndroids

###Animator Fragment Transitions

This fork allows using [NineOldAndroids](http://nineoldandroids.com) Object Animators for custom fragment transitions.  Note that animators will NOT be loaded from theme settings or by transitions settings; only custom transitions work.  Also View animations will no longer work.

    tx.setCustomTransitions(android.R.animator.fade_in, android.R.animator.fade_out)

###PageTransformer

ViewPager is modified to support custom PageTransformers implemented with NineOldAndroids AnimatorProxy.  For example

```java
public class ZoomOutPageTransformer implements PageTransformer {
    private static float MIN_SCALE = 0.85f;
    private static float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        
        AnimatorProxy proxy = AnimatorProxy.wrap(view);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            proxy.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                proxy.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                proxy.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            proxy.setScaleX(scaleFactor);
            proxy.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            proxy.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            proxy.setAlpha(0);
        }
    }
}
```

Set the PageTransformer as usual:

    viewPagers.setPageTransformer(new ZoomOutPageTransformer());
