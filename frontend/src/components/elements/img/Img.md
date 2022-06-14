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
The image component also uses native browser lazy loading to stop unnecessary downloads for the user. 

Use the `srcset` attribute to serve different sizes of the same image so the browser 
can show the image best suited for the viewport where it is displayed. The `sizes` attribute helps 
to choose the correct image based on the conditions given. 

Where possible, each image should also have a low quality image placeholder (LQIP) 
to be shown to the user whilst the high resolution image loads. This can be passed 
in using the `lowResImg` prop. 

  ```jsx
    <VsImg
        srcset="../../../fixtures/related-content/images/city-country-breaks.jpg?size=xs 300w, 
                ../../../fixtures/related-content/images/city-country-breaks.jpg?size=sm 600w,
                ../../../fixtures/related-content/images/city-country-breaks.jpg?size=md 1200w, 
                ../../../fixtures/related-content/images/city-country-breaks.jpg?size=lg 2048w"
        sizes="(min-width: 768px) 75vw, 100vw"
        low-res-img="../../../fixtures/related-content/images/city-country-breaks.jpg?size=xxs"
        src="../../../fixtures/related-content/images/city-country-breaks.jpg"
        alt="City country breaks"
    />
 ```

## Accessibility
See the images section within the <a href="/#/Guidelines/Accessibility">accessibility guidelines</a> for more information. 