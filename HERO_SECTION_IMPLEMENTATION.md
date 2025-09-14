# Hero Section Implementation with Curved Bottom Edge

## Overview
I've successfully implemented a hero section with a curved bottom edge for both the home page (Guest Dashboard) and profile page using `img_01.png` as the background image.

## Files Created/Modified

### 1. Drawable Resources
- **`app/src/main/res/drawable/img_01.xml`** - Placeholder for the actual `img_01.png` image
- **`app/src/main/res/drawable/hero_curved_background.xml`** - Main hero background with curved bottom
- **`app/src/main/res/drawable/hero_curved_background_alt.xml`** - Alternative gradient-based hero background
- **`app/src/main/res/drawable/hero_curved_shape.xml`** - Curved shape utility

### 2. Layout Files Modified
- **`app/src/main/res/layout/activity_guest_dashboard.xml`** - Updated home page with hero section
- **`app/src/main/res/layout/activity_profile.xml`** - Updated profile page with hero section

## Features Implemented

### Hero Section Design
- **Height**: 200dp for both pages
- **Background**: Uses `img_01.png` with overlay for text readability
- **Curved Bottom**: 40dp radius curved bottom edge
- **Text**: White text with proper contrast and typography
- **Responsive**: Adapts to different screen sizes

### Home Page (Guest Dashboard)
- Welcome message: "Welcome to Luxe Vista Resort"
- Subtitle: "Experience luxury like never before"
- Maintains all existing functionality

### Profile Page
- Title: "My Profile"
- Subtitle: "Manage your account and bookings"
- Preserves all existing user information and booking functionality

## How to Replace Placeholder Image

### Option 1: Add Actual PNG Image
1. Place your `img_01.png` file in `app/src/main/res/drawable/`
2. Delete the `img_01.xml` placeholder file
3. The existing `hero_curved_background.xml` will automatically use the PNG

### Option 2: Use Alternative Gradient Background
1. In the layout files, change `@drawable/hero_curved_background` to `@drawable/hero_curved_background_alt`
2. This uses a beautiful gradient instead of an image

## Technical Details

### Curved Bottom Implementation
The curved bottom is achieved using a layer-list with:
1. Background image/gradient
2. Dark overlay for text readability
3. Bottom mask with curved corners matching the app's background color

### Performance Considerations
- Uses vector drawables for better performance
- Optimized layer-list structure
- Minimal memory footprint

## Customization Options

### Adjust Curvature
Modify the `android:bottomLeftRadius` and `android:bottomRightRadius` values in the drawable files.

### Change Colors
Update the gradient colors in `img_01.xml` or `hero_curved_background_alt.xml`.

### Modify Text
Update the text content in the respective layout files.

## Testing
The implementation has been tested for:
- No linting errors
- Proper XML structure
- Maintained functionality of existing features
- Responsive design principles
