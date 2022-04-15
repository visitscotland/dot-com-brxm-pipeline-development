## Usage
Use this component when displaying any body text or HTML content coming from a WYSIWYG editor. 

### Default
The default style sets the main paragraph text size which is then reduced at smaller breakpoints. 

```jsx
    <VsRichTextWrapper>
        <VsHeading level="3">
            Experiencing Ben Nevis
        </VsHeading>
        <p>
            Scotland's landscape is scattered with Munros and mist-shrouded hills... But Ben Nevis is the king of them all. In the north west Highlands, near the town of <VsLink href="#">Fort William</VsLink> and part of the Grampian Mountain range, the famous peak attracts 125k walkers a year. 
        </p>
        <p>
            Whether you're an avid ambler or you just love beautiful landscapes, bagging 'the Ben' is likely to feature near the top of your Scottish bucket list.
        </p>
    </VsRichTextWrapper>
```

### Lead 
The lead variant is used to make text larger and stand out, commonly used for introductions and other lead paragraphs. 

```jsx
    <VsRichTextWrapper variant="lead">
        <VsHeading level="3">
            The mountain with its head in the clouds
        </VsHeading>
        Ben Nevis requires little introduction. With a wild heart, an adventurous spirit and a flair for drama, the legendary peak towers above glistening lochans and deep glacial valleys. In Scotland, you can't get any higher than this.
    </VsRichTextWrapper>
```
