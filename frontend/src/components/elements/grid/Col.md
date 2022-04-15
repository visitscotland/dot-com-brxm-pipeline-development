## Usage
In this grid system, we have 12 columns which each have a gutter to maintain correct spacing. 
A column component must aways be a direct child of the row component. 

  ```jsx
    <div class="style-cols">
        <VsContainer>
            <VsRow>
                <VsCol cols="8" sm="12">
                    <p>cols="8" sm="12"</p>
                </VsCol>
            </VsRow>
            <VsRow>
                <VsCol cols="12" lg="8">
                    <p>cols="12" lg="8"</p>
                </VsCol>
                <VsCol>
                    <p>no cols props</p>
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
  ```