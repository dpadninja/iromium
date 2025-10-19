# Iromium - Android/Google TV App That Controls BLE Light Strip

## Why This Project Exists
Iromium was created to turn a cheap Chinese LED controller into a fully integrated TV accessory.  
The main goal was simple: make the LED strip behave like part of the TV — turn on/off with the TV and allow basic control directly from the TV screen.  
Existing apps either require a phone or are too complicated for this simple use case, so I made a lightweight app that does exactly what’s needed.

## Origin of the Name
The name **Iromium** comes from *iromi* (いろみ), which can mean tone, hue, color, or tint.

## Features
- **Unified Control** – Power on/off your TV and LED strip at the same time — turning off the TV automatically turns off the LEDs, and vice versa.
- **Custom Presets** – Adjust colors easily to perfectly match your LED strips.
- **Color Calibration** – Fine-tune color balance for the most natural and vibrant lighting experience.
- **Compatibility** – Works with Android/Google TV >=11.

## LED Controller/Strip Choosing Guide
Picking the right LED strip can feel tricky, but it’s easier once you know what to look for. Here’s a simple guide to help you choose the best strip for your setup.

On marketplaces like **AliExpress** or **Amazon**, you’ll find countless non-branded kits that include a controller and LED strip, sometimes bundled with a simple IR remote. They may look different and be sold under various names, but in reality, they are usually the same hardware.

Most of these controllers work with several popular mobile apps (this can also help you identify compatible devices):
- Lotus Lantern, Magic Lantern
- DuoCo Strip, DuoCo StripX
- LED LAMP
- HappyLighting
- Magic-LED
- LED Hue
- BanlanX
- Colorful Lights
- MR Star

Here’s a concise, effective search prompt you can use on marketplaces:
```
'led strip bluetooth 12v'
```  
You can add the app name like 'Lotus Lantern' to narrow results.

### Recommended Options
- Choose a kit that includes **Bluetooth connectivity**.
- Use an **external power supply** (not USB).
- Prefer **12V or higher** LED strips — they’re brighter and more durable.
- Ensure the strip is long enough for your TV setup.

### Options to Avoid
- **USB-powered LED strips**
  > While they technically work, they are limited to **5V**, which makes them too dim.
- **RGBW, RGBCCT, RGBWW, or addressable strips**
  > They may work but are officially not supported yet.

## Using the App

### How to Connect
1. Power on the LED controller.
2. Obtain the MAC address of your controller (skip steps 2-3 if you already have it):
    - **Scan for Bluetooth Low Energy devices:**
        - Android: use [nRF Connect](https://play.google.com/store/apps/details?id=no.nordicsemi.android.mcp)
        - Windows: use [nRF Connect for Desktop](https://www.nordicsemi.com/Products/Development-tools/nRF-Connect-for-Desktop)
        - Linux:
          ```bash
          bluetoothctl
          [bluetoothctl]> scan on
          ```  
      > iOS cannot reveal the real MAC address when scanning BLE devices.
    - Possible device names:
        - `ELK-BLEDOM`, `ELK-BLEDOB`, `ELK-HR-RGB`
        - `MELK-OA`
        - `LED-DMX-00`, `LED-DMX-03`
        - `Triones`
        - `SP105E`, `SP110E`, `SP611E`, `SP621E`
        - `Colorful-Light`
        - `GATT--DEMO`
    - Find the **MAC address** (six hexadecimal numbers separated by colons, e.g., `XX:XX:XX:XX:XX:XX`).
3. Open the TV app, enter the MAC address using your remote, and confirm.
4. Follow the on-screen instructions to complete setup.

### Main Screen
After setup, you'll see the main window with colorful, rounded buttons:
- **Short press** a button to apply the color.
- **Long press** a button to edit the color.

By default, the app turns the LED on/off along with the TV. You can change this behavior in the settings.

### Edit Preset
This screen shows the levels of each LED channel — red, green, and blue.  
Adjust them to see how the LED color changes in real time.  
Below, five rounded buttons represent label colors — choose the one that best matches the LED color you see.

### Color Calibration
The app includes a per-channel calibrator to help achieve more natural colors.
> Tip: By default, blue and green LEDs may appear too bright. Slightly reduce their intensity for a more balanced look.

## Projects Without Which This Project Would Not Exist
- [ELK-BLEDOM by FergusInLondon](https://github.com/FergusInLondon/ELK-BLEDOM)
