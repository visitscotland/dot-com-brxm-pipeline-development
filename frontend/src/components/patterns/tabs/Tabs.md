## Usage

The *VsTabs* component should contain two or more *VsTabItems*, each of which takes a `title` prop
and contains a slot for the content of that tab.

```js
    <VsTabs>
        <VsTabItem title="Edinburgh">
            <div class="px-5 py-4">
                <p>
                    With an abundance of history, top quality attractions and beautiful
                    architecture, Edinburgh is a city that you won’t forget in a hurry. Explore
                    the Medieval Old Town and the elegant Georgian New Town, which sit side by
                    side, and visit unique attractions throughout the city on a holiday in
                    Edinburgh.
                </p>
                <p>
                    Whether you’re short of time and can only manage a weekend break, planning
                    a full-blown city break or are visiting Edinburgh as part of a wider holiday
                    in Scotland, there’s lots to fill your time, from unforgettable historic
                    sites and world-class festivals to fantastic shopping and mouth-watering
                    dining.
                </p>
            </div>
        </VsTabItem>
        <VsTabItem title="Glasgow">
            <div class="px-5 py-4">
                <p>
                    From dawn to dusk and into the night, there are a huge range of things to do in
                    Glasgow during every hour of your trip. Glasgow is home to some of Scotland's
                    best cultural attractions and best of all, most of them are completely free!
                </p>
                <p>
                    You could easily fill your time exploring the different neighbourhoods, and
                    enjoying the amazing shopping, dining and attractions in Glasgow. Maybe you'll
                    explore Glasgow's music scene on a city walking tour, or uncover countless
                    treasures inside its fantastic museums and art galleries?
                </p>
            </div>
        </VsTabItem>
    </VsTabs>
```

When to use – Tabs can help break up information making content easier to read and should be used
to group related content into categories. They can be used to organise content so a user doesn’t
have to navigate away from the page.

Never use tabs for primary navigation, to indicate progress or when a user needs to compare
information.

Tabs should contain at least two items and only one tab is active at a time. When the page is
loaded, the first tab should always be selected.

## Accessibility
When JavaScript is disabled the content of each tab is vertically stacked with all tabs visible at
the same time.

The component uses appropriate ARIA attributes to help users of assistive technologies, and is
navigable with the directional keys in line with WCAG guidelines.


