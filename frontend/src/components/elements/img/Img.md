## Usage
All images must follow the correct aspect ratio as outlined in 
<a href="/#/Guidelines/Images">Images</a> guidelines. All images must have an alt 
text attribute present although, for decorative images, the alt 
text can be left blank.

### Default
The default image display just requires a `src` and `alt` text.
  ```jsx
    <VsImg
        src="../../../fixtures/related-content/images/city-country-breaks.jpg"
        alt="City country breaks"
    />
  ```

### Fluid
Use `fluid` to make the image responsive, it will scale with the parent up to a 
max of the native width of the image itself. Use `fluid-grow` to scale beyond the native 
image width if necessary.

  ```jsx
    <VsImg
        fluid
        src="../../../fixtures/related-content/images/city-country-breaks.jpg"
        alt="City country breaks"
        class="mb-4"
    />

    <VsImg
        fluid-grow
        src="../../../fixtures/related-content/images/city-country-breaks.jpg"
        alt="City country breaks"
    />
 ```

### Performance
To ensure high performance, images should be compressed so they are loaded quickly.
Images should also be lazy loaded to stop unnecessary downloads for the user. 

We can use `srcset` to serve different sizes of the same image so the browser 
can show the image best suited for the viewport where it is displayed.

## Accessibility
See the images section within the <a href="/#/Guidelines/Accessibility">accessibility guidelines</a> for more information. 