## Usage
The breadcrumb component is a set of links used to help a user find their way around the website and switch between different levels in the hierarchy. 

Breadcrumbs should be displayed at the top of the page beneath the main navigation and the first link should be the home page. The last item should be the current page and should be text, not a link. 

### Standard
The standard breadcrumb shows a list of separated links. 

```jsx
    <VsBreadcrumb>
        <VsBreadcrumbItem
            v-for="(item, index) in breadcrumb.breadcrumb"
            :key="index"
            :href="item.href"
            :active="item.active"
            :text="item.name"
        />
    </VsBreadcrumb>
```

### Short
On mobile, the breadcrumb is shortened to avoid wrapping to multiple lines. We show the current page and previous page so the user has the option to go back a level. Note: resize the browser to see this working. 

```jsx
    const breadcrumbSmall = require("../../../assets/fixtures/breadcrumb/breadcrumb-mobile.json");

    <VsBreadcrumb>
        <VsBreadcrumbItem
            v-for="(item, index) in breadcrumbSmall"
            :key="index"
            :href="item.href"
            :active="item.active"
            :text="item.name"
        />
    </VsBreadcrumb>
```

## Accessibility
The breadcrumb component uses a `nav` element and `aria-label=”breadcrumb”` for assistive technologies to tell the user they have entered a breadcrumb. The `aria-current=”page”` attribute is also added to the last item to indicate this is the current page.
