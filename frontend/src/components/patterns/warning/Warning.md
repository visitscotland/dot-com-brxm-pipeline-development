## Usage
Use the warning component to display a warning message aimed at the end user. It contains a message
with an optional second line and optional button.

Various options can be defined controlling the font size, alignment, theme, transparency and icon.

Automatically expands to fill whatever container it is placed in - usually
covering or replacing an existing element.

### Default
The default layout uses a semi-transparent dark background with secondary theme styling on the text and icon.
Text is set to the 'normal' size and aligned to the left.

```js
    <div
        class="position-relative mb-4"
        style="width: 40em; height: 20em;"
    >
        <VsWarning>
            JavaScript is needed to watch this video.
        </VsWarning>
    </div>
```
### Additional text
To give the user further information, another paragraph of text can be added using a slot.
This is displayed in a slightly smaller font size.

```js
    <div
        class="position-relative"
        style="width: 40em; height: 24em;"
    >
        <VsWarning>
            JavaScript is needed to watch this video.

            <template slot="extra-content">
                You can turn on Javascript in your browser settings.
            </template>
        </VsWarning>
    </div>
```

### Button
A button can be added by using a slot. If the prop `type` is set to `cookie`, this
will render a button which opens to OneTrust's preference centre.

```js
    <div
        class="position-relative mb-12"
        style="width: 25em;"
    >
        <VsWarning
            type="cookie"
        >
            You need cookies enabled to view this content.

            <template slot="button-text">
                Manage cookies settings
            </template>
        </VsWarning>
    </div>
```

### Themes
The component can use either a light or dark theme. The dark theme is used by default.

```js
    <div
        class="position-relative"
        style="width: 40em; height: 24em;"
    >
        <VsWarning
            theme="dark"
        >
            JavaScript is needed to watch this video.

            <template slot="extra-content">
                You can turn on Javascript in your browser settings.
            </template>
        </VsWarning>
    </div>
```

```js
    <div
        class="position-relative"
        style="width: 40em; height: 24em;"
    >
        <VsWarning
            theme="light"
        >
            JavaScript is needed to watch this video.

            <template slot="extra-content">
                You can turn on Javascript in your browser settings.
            </template>
        </VsWarning>
    </div>
```


### Alignment 
The component text and icon can be aligned left or right. Right alignment should
be used when the Warning component is placed on the right side of its parent component.

```js
    <div
        class="position-relative"
        style="width: 40em; height: 24em;"
    >
        <VsWarning
            align="right"
        >
            JavaScript is needed to watch this video.
        </VsWarning>
    </div>
```

### Transparency 
By default, the dark theme uses an 20% transparency to allow it to sit over images. This contain
be turned off using the transparency prop.

Please note that the light theme is always opaque to ensure contrast for accessibility.

```js
    <div
        class="position-relative"
        style="width: 40em; height: 24em;"
    >
        <VsWarning
            :transparency="false"
        >
            JavaScript is needed to watch this video.
        </VsWarning>
    </div>
```

### Size 
Font, icon and button sizes can be controlled using the `size` prop. The `small` size should be used
when there is less space in the parent component.

```js
    <div
        class="position-relative"
        style="width: 30em;"
    >
        <VsWarning
            size="small"
        >
            You need cookies enabled to view this content.

            <template slot="button-text">
                Manage cookies settings
            </template>
        </VsWarning>
    </div>
```

### Icon 
The `review` icon is used by default. This can be overriden by the `icon` prop.

```js
    <div
        class="position-relative"
    >
        <VsWarning
            icon="clock"
        >
            You have 10 minutes before this form times out.
        </VsWarning>
    </div>
```
## Accessibility

- Ensure that the reason for the warning message appearing is clear

- Only use decorative images behind the transparent warning

- The button text should clearly state its purpose
