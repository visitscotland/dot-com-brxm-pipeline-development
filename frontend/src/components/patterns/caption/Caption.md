## Usage
Captions should be used with an image or video to describe it and give credit to the creator. 

### Full Width
The full width caption is the default and should match the width of the media it’s related to. It contains the media title and credit. 

```js
    <VsCaption>
        <template slot="caption">
            A Scottish Castle
        </template>

        <template slot="credit">
            &copy; VisitScotland
        </template>
    </VsCaption>
```

### Alignment
The caption can be aligned to the right using the `text-align` prop.
```js
    <VsCaption text-align="right">
        <template slot="caption">
            A Scottish Castle
        </template>

        <template slot="credit">
            &copy; VisitScotland
        </template>
    </VsCaption>
```

### Social Credit Link
If the image or video has come from social media, this should be indicated with a link back to the user's profile using a <a href="#/Patterns/Social%20Credit%20Link">SocialCreditLink</a>.
```js
    <VsCaption>
        <template slot="caption">
            A Scottish Castle
        </template>

        <VsSocialCreditLink
            slot="credit"
            credit="VisitScotland"
            socialPostUrl="http://www.visitscotland.com"
            source="instagram"
        >
        </VsSocialCreditLink>
    </VsCaption>
```


### Large Caption
The large caption variant is used for larger media such as Hero image or Listicles and includes a map with the location of the image if latitude and longitude are provided.
```js
    <VsCaption
        latitude="55.9485947"
        longitude="-3.2021022"
        variant="large"
    >
        <template slot="caption">
            A Scottish Castle
        </template>

        <template slot="credit">
            &copy; VisitScotland
        </template>
    </VsCaption>
</BsWrapper>
```