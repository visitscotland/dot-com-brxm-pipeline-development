## Usage
Links are navigational elements used to move to a section on a page or 
to another page or website. They can be used inline with copy text or on their own. 

Links should be short, clear and labelled to accurately reflect the destination. 

They should not be used for triggering actions like submitting a form or opening a 
modal, a <a href="/#/Elements/Button">button</a> should be used for these purposes. 


### Standalone
By default, all links have an underline. Standalone links can be used on their own or following content. 
```jsx
    <VsLink href="#">
        See all accommodation
    </VsLink>
```

### Inline
Inline links are used within text and paragraphs and should match the font size of the text it is inline with.
```jsx
    <p>
        From <VsLink href="#">budget-friendly campsites</VsLink> and hostels to exclusive self-catering for 
        large groups, or stylish city centre hotels, you'll be spoilt 
        for choice with Scottish accommodation!
    </p>
```

### Icons
We have three types of links which use icons. Often used in card links, 
this can help a user understand and navigate the site better. 


```jsx
    <BsWrapper class="mb-9">
        <VsHeading level="4">External Link</VsHeading>
        <p>Used when taking a user away from your website to an external URL.<br/>
            <VsLink type="external" href="#">
                See all accommodation
            </VsLink>
        </p>
    </BsWrapper>

    <BsWrapper class="mb-9">
        <VsHeading level="4">Internal Link</VsHeading>
        <p>Used when taking a user to a link within your website.<br/>
            <VsLink type="internal" href="#">
                See all accommodation
            </VsLink>
        </p>
    </BsWrapper>

    <BsWrapper class="mb-9">
        <VsHeading level="4">Download Link</VsHeading>
        <p>Used when a user is downloading a file such as a PDF.<br/>
            <VsLink type="download" href="#">
                See all accommodation
            </VsLink>
        </p>
    </BsWrapper>
```



### Dark Theme
When using links on a dark theme background, use the `on-dark` link variant for better colour contrast. 
```jsx
    <BsWrapper class="d-flex flex-wrap bg-dark p-4 mb-4">
        <VsLink variant="on-dark" class="mb-6" href="#">
            See all accommodation
        </VsLink>

        <VsRichTextWrapper>
            <p style="color: #fff;">
                From <VsLink variant="on-dark" type="external" href="#">budget-friendly campsites</VsLink> and hostels to exclusive self-catering for 
                large groups, or stylish city centre hotels, you'll be spoilt 
                for choice with Scottish accommodation!
            </p>
        </VsRichTextWrapper>
    </BsWrapper>
```