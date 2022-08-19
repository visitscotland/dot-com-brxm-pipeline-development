## Usage
The site search component sits in the main navigation bar in the page header and enables
users to search the site using a keyword or phrase to find relevant content using the form. 

### Search Toggle 
In the main navigation, when the search button is clicked, the site search form will appear 
below it and change state to show the button is active. Clicking the button again or the close
button within the form will hide the form again. 

  ```jsx
    <VsMegaNav
        search-button-text="Search"
        href="/"
        menu-toggle-alt-text="Toggle Menu"
    >
    </VsMegaNav>
  ```


### Search Form 
The user can enter keywords they want to find and click the search button to trigger
a new search. The user also has the option to clear the text entered by clicking the button 
that appears on input. 

```jsx
    <VsSiteSearch
        :is-showing="true"
    >
        Search
    </VsSiteSearch>
    <VsSiteSearchForm 
        label-text="What are you looking for?"
        submit-button-text="Search"
        clear-button-text="Clear form"
        close-button-text="Close search form"
    />
  ```

## Accessibility
We provide helpful placeholder text that also matches the label for the input
that will be read out by a screenreader only. We also provide screenreader only 
text for all buttons with only icons. 