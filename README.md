## A very lightweight version of the popular Balloon library.

A minimal number of properties and methods are available.

![Balloon-Simple](https://github.com/user-attachments/assets/d4a70402-bc61-4408-9c6e-98b4f2922235)

To use the ready-made library, add the dependency:
```
dependencies {

    implementation("io.github.uratera:balloon_simple:1.0.1")
}
```

Attributes	|Description
----------------------------------|--------------------------------------------------------
.build	|Create a new instance of the popup
.setArrowSize	|Arrow size
.setBackgroundColor	|Background color of the arrow and the popup. Black by default.
.setCornerRadius	|Radius of the rounding of the corners of the window
.setHeight	|Window height
.setLayout	|Custom layout
.setIsVisibleArrow	|Arrow visibility
.setWidth	|Window width


Methods	|Description
-------------------------------------|--------------------------------------------------
dismiss	|Close window
dismissWithDelay	|Automatically close window with millisecond delay
showBalloon	|Show popup window

