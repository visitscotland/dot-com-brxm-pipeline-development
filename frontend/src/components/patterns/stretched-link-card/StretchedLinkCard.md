## Usage

A *StretchedLinkCard* takes a `link` prop defining where the card should navigate to, as well as a
`type` prop. The `type` provides a visual indicator next to the link signposting the expected
behaviour when that link is selected.

The entire card, including all image and text content, will function as a link rather than the user
being forced to click on the title.

It also contains a `stretchedCardHeader` slot where the main title associated with the link should
go, and an optional `stretchedCardContent` slot providing body copy for the link.

```js
    <VsStretchedLinkCard
        link="https://visitscotland.com"
        type="external"
    >
        <template slot="stretchedCardHeader">
            Dunfermline Restaurants
        </template>
    </VsStretchedLinkCard>

    <VsStretchedLinkCard
        link="https://visitscotland.com"
        type="internal"
    >
        <template slot="stretchedCardHeader">
            Perth Hotels
        </template>

        <VsRichTextWrapper slot="stretchedCardContent">
            <p>A former capital of Scotland, the city of Perth is steeped in Scottish history.</p>
            
            <p>Located alongside Scotland's longest river, the Tay, its central location makes it an
            ideal spot from which to explore many parts of Scotland.</p>
        </VsRichTextWrapper>
    </VsStretchedLinkCard>
```

The card will expand to fill the full width of it's container so it should usually be placed in a
container with a defined, responsive width.

```js
    <VsCol cols="12" md="6">
        <VsStretchedLinkCard
            link="https://visitscotland.com"
            type="internal"
        >
            <template slot="stretchedCardHeader">
                Perth Hotels
            </template>

            <VsRichTextWrapper slot="stretchedCardContent">
                <p>A former capital of Scotland, the city of Perth is steeped in Scottish history.</p>
                
                <p>Located alongside Scotland's longest river, the Tay, its central location makes it an
                ideal spot from which to explore many parts of Scotland.</p>
            </VsRichTextWrapper>
        </VsStretchedLinkCard>
    </VsCol>
```

If an image is provided it will fill the full width of the card, retaining it's native aspect ratio.

```js
    <VsCol cols="12" md="6">
        <VsStretchedLinkCard
            link="https://visitscotland.com"
            type="external"
            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            imgAlt="This is the alt text"
        >
            <template slot="stretchedCardHeader">
                Stirling Tips
            </template>
        </VsStretchedLinkCard>
    </VsCol>
```

Additional contextual information can be provided by setting a category for the card in the
`stretchedCardCategory` slot, especially if the card is appearing alongside a large number of
others in a search result. A *VsStretchedLinkPanels* component can also be added in the
`stretchedCardPanels` slot, displaying duration and transport information for a trip or itinerary.

The `stretchedCardPanels` should only be used if an image is present on the card, otherwise it can
overlap with the text of the card.

```js
    <VsCol cols="12" md="6">
        <VsStretchedLinkCard
            link="https://visitscotland.com"
            type="external"
            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            imgAlt="This is the alt text"
        >
            <template slot="stretchedCardCategory">
                Walks
            </template>

            <template slot="stretchedCardPanels">
                <VsStretchedLinkPanels
                    days="14"
                    daysLabel="days"
                    transport="car"
                    transportName="Car"
                />
            </template>

            <template slot="stretchedCardHeader">
                Inverness Walks
            </template>
        </VsStretchedLinkCard>
    </VsCol>
```

## Accessibility

The content of each card is accessible via tab navigation, with the main title of the link - set
with the `stretchedCardHeader` slot - serving as the initial entry point for the user.
