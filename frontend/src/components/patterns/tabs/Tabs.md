## Usage

The Tabs component is a wrapper which can contain any number of *VsTabItem* components, with a
`title` property setting the tab title and the content placed in as a slot. Each of them will
be rendered as a tab with the first one open by default. 

```js
    <VsContainer class="mt-7">
        <VsRow>
            <VsCol>
                <VsTabs>
                    <VsTabItem title="First">
                        <div class="px-5 py-4">
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing
                                elit. Sed porta nec mauris id vulputate. Nullam hendrerit
                                libero pharetra dolor iaculis, ac feugiat nisi pharetra.
                                Aliquam erat volutpat.
                            </p>
                            <p>
                                Suspendisse rhoncus turpis vel felis consectetur consectetur.
                                Pellentesque justo dolor, accumsan eget tincidunt sit amet,
                                ultricies tempus urna. Pellentesque convallis tincidunt
                                pellentesque. Donec rhoncus mi non dolor lacinia dapibus.
                                Aenean vehicula tempor fringilla. Morbi nibh nulla, ornare nec
                                lacus at, gravida pharetra mi.
                            </p>
                        </div>
                    </VsTabItem>
                    <VsTabItem title="Second">
                        <div class="px-5 py-4">
                            <p>
                                Proin vitae ultrices nibh. Nulla scelerisque mi vitae cursus
                                vulputate. In hac habitasse platea dictumst. Praesent faucibus
                                sit amet magna nec imperdiet. Donec a tristique est,
                                eu imperdiet elit.
                            </p>

                            <p>
                                Pellentesque auctor eu lorem ut aliquet. Nulla lobortis
                                mauris ligula, sit amet volutpat purus tincidunt at. Fusce
                                dui sapien, malesuada apellentesque at, placerat vel nisi.
                                Aliquam sit amet velit maximus risus lacinia
                                aliquam eget ut arcu.
                            </p>
                        </div>
                    </VsTabItem>
                </VsTabs>
            </VsCol>
        </VsRow>
    </VsContainer>
```

## Accessibility

When JavaScript is disabled the content of each tab is vertically stacked with all tabs visible at
the same time.

```js
    <VsContainer class="mt-7 no-js">
        <VsRow>
            <VsCol>
                <VsTabs>
                    <VsTabItem title="First">
                        <div class="px-5 py-4">
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing
                                elit. Sed porta nec mauris id vulputate. Nullam hendrerit
                                libero pharetra dolor iaculis, ac feugiat nisi pharetra.
                                Aliquam erat volutpat.
                            </p>
                            <p>
                                Suspendisse rhoncus turpis vel felis consectetur consectetur.
                                Pellentesque justo dolor, accumsan eget tincidunt sit amet,
                                ultricies tempus urna. Pellentesque convallis tincidunt
                                pellentesque. Donec rhoncus mi non dolor lacinia dapibus.
                                Aenean vehicula tempor fringilla. Morbi nibh nulla, ornare nec
                                lacus at, gravida pharetra mi.
                            </p>
                        </div>
                    </VsTabItem>
                    <VsTabItem title="Second">
                        <div class="px-5 py-4">
                            <p>
                                Proin vitae ultrices nibh. Nulla scelerisque mi vitae cursus
                                vulputate. In hac habitasse platea dictumst. Praesent faucibus
                                sit amet magna nec imperdiet. Donec a tristique est,
                                eu imperdiet elit.
                            </p>

                            <p>
                                Pellentesque auctor eu lorem ut aliquet. Nulla lobortis
                                mauris ligula, sit amet volutpat purus tincidunt at. Fusce
                                dui sapien, malesuada apellentesque at, placerat vel nisi.
                                Aliquam sit amet velit maximus risus lacinia
                                aliquam eget ut arcu.
                            </p>
                        </div>
                    </VsTabItem>
                </VsTabs>
            </VsCol>
        </VsRow>
    </VsContainer>
```
