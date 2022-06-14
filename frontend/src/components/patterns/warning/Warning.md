## Usage
Use the warning component to display a dark generic warning message aimed
at the end user. It can contain a message and a link to a page the user can go
to to correct the issue.

Automatically expands to fill whatever container it is placed in - usually
covering or replacing an existing element

  ```js
    <div
        class="position-relative"
        style="width: 12em; height: 12em;"
    >
        <VsWarning
            warningMessage="JavaScript is needed to watch this video."
        />
    </div>
    <br />
    <div
        class="position-relative"
        style="width: 15em; height: 18em;"
    >
        <VsWarning
            warningMessage="Cookies are needed to watch this video."
            :warningLink="{
                url: 'https://google.com',
                label: 'Update my cookie settings'
            }"
        />
    </div>

  ```
