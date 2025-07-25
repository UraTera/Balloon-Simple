## A very lightweight version of the popular Balloon library.

A minimal number of properties and methods are available.

![Balloon](https://github.com/user-attachments/assets/0a5d1ae3-1537-41b2-9d98-32e646de001c)

To use the ready-made library, add the dependency:
```
dependencies {

    implementation("io.github.uratera:balloon_simple:1.1.0")
}
```

Attributes	|Description 
----------------------------------|---------------------------------------------
.build	|Create a new instance of the popup
.setAnimation	|Enable animation (false)
.setArrowSize	|Arrow size  (12dp)
.setBackgroundColor	|Background color of the arrow and the popup (white)
.setCornerRadius	|Window corners rounding radius (5dp)
.setHeight	|Window height
.setLayout	|Custom layout
.setIsVisibleArrow	|Arrow visibility (true)
.setWidth	|Window width

Methods	|Description
-----------------------------------|--------------------------------------------------
content	|Custom layout content
dismiss	|Close window
dismissWithDelay	|Automatically close window with millisecond delay

Show up Balloon
```
// Show balloon in the center of the screen
showCenter()
// Show balloon in the center of the screen with offset x-off and y-off
showCenter(xOff: Int, yOff: Int)
// Show the balloon over anchor view
showTop(anchor: View) 
// Show the balloon over anchor view with offset x-off and y-off
showTop(anchor: View, xOff: Int, yOff: Int)
```

