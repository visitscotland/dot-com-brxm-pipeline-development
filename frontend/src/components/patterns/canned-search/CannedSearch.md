## Usage
The canned search component is a carousel of product cards that are populated using an API endpoint. There are 5 types of products that can be displayed, these are Tours, Accommodation, Events, Things to do and Food & drink. 

Each product card follows the same pattern where the whole card is a link which will take the user to view more details about a product. They also include a footer section for extra information with an external link to more product details. 

### Standard 
Each canned search component should include a `heading` and an `apiUrl` to retrieve products. It can also include optional introduction text and call to action button that links to a full list. 

Add a `searchType` to define which product cards to display. If no type is specified, the default is accommodation.

Screenreader text for carousel buttons must also be added using the required props. 

```jsx
    <VsCannedSearch
        api-url="http://172.28.81.65:8089/data/component/cannedsearch?prodtypes=acco&avail=off&locplace=4751&locprox=10.0&loc=Glasgow&fac_id=accessguide"
        heading="Accommodation in Glasgow"
        search-type="acco"
        carousel-next-text="Next slide"
        carousel-previous-text="Previous slide"
    >
        <template slot="vsCannedSearchIntro">
            <p>Find your perfect place to stay in Glasgow.</p>
        </template>

        <template slot="vsCannedSearchButtons">
            <VsButton href="#">
                View All Accommodation
            </VsButton>
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
```

### Search Credit
There is a slot available to add a credit at the bottom of the carousel for any third party search.

```jsx
    <VsCannedSearch
        apiUrl="http://172.28.81.65:8089/data/component/cannedsearch?prodtypes=even&locplace=&locprox=10.0&loc=Scotland"
        heading="Events in Scotland"
        searchType="even"
        carousel-next-text="Next slide"
        carousel-previous-text="Previous slide"
    >
        <template slot="vsCannedSearchCredit">
            Third party search credit
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
```

### Other Examples
More examples of different types of searches that can be displayed including Food & Drink, Things to Do and Tours. 

```jsx
    <VsCannedSearch
        apiUrl="http://172.28.81.65:8089/data/component/cannedsearch?prodtypes=cate&locpoly=821&locprox=10.0&loc=Royal+Mile"
        searchType="cate"
        heading="Food & Drink"
        carousel-next-text="Next slide"
        carousel-previous-text="Previous slide"
    >
        <template slot="vsCannedSearchButtons">
            <VsButton href="#">
                View All Food & Drink
            </VsButton>
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
```

```jsx
    <VsCannedSearch
        apiUrl="http://172.28.81.65:8089/data/component/cannedsearch?prodtypes=acti%2Cattr%2Creta&locplace=4751&locprox=10.0&loc=Glasgow"
        searchType="acti"
        heading="Things to Do"
        carousel-next-text="Next slide"
        carousel-previous-text="Previous slide"
    >
        <template slot="vsCannedSearchButtons">
            <VsButton href="#">
                View All Things to Do
            </VsButton>
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
```

```jsx
    <VsCannedSearch
        apiUrl="http://172.28.81.65:8089/data/component/cannedsearchtours?find%5B%5D=attractions%7Caberdeen%7CAberdeen&locale=en-GB"
        searchType="tour"
        heading="Tours in Aberdeen"
        carousel-next-text="Next slide"
        carousel-previous-text="Previous slide"
    >
        <template slot="vsCannedSearchButtons">
            <VsButton href="#">
                View All Tours
            </VsButton>
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
```
