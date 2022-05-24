## Usage
The banner component sits below the main navigation at the top of a page across its full width highlighting important information for all users to see. It pushes content down rather than sitting on top of other elements.   

The banner must contain a title and call to action link to more information. It may also include extra text content for a better description.  

Only one banner should ever be displayed at one time.

```jsx
    <VsBanner
        close-btn-text="Close"
        title="Covid-19 Travel Advice"
        :dont-show-again="false"
    >
        <template slot="bannerText">
            <p>
                Find the latest information on travel, and Good to Go (Covid-safe)
                businesses. 
            </p>
        </template>

        <template slot="bannerCta">
            <VsLink href="#">
                View Covid-19 Travel Advice
            </VsLink>
        </template>
    </VsBanner>
  ```

### Removing the Banner
By default, a user can remove the banner from a page using the close button and if the user allows the setting of cookies, the banner won’t reappear until their next session. 

The `dontShowAgain` prop can be set to false so that a cookie is never set and the banner will reappear every time the page is reloaded. 


## Accessibility
As the close button in this component is icon-only, accessible screenreader-only text must be provided via the `close-btn-text` prop.