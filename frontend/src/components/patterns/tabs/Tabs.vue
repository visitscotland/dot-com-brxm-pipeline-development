<template>
    <div
        class="vs-tabs"
        data-test="vs-tabs"
    >
        <BTabs>
            <!-- @slot default slot for VsTabItems -->
            <slot />
        </BTabs>
    </div>
</template>

<script>
import { BTabs } from 'bootstrap-vue';
/**
 * Tabs wrapper used with TabItems.
 *
 * @displayName Tabs
 */
export default {
    name: 'VsTabs',
    components: {
        BTabs,
    },
};
</script>

<style lang="scss">
</style>

<docs>
  ```js
    <VsContainer class="mt-7">
        <VsRow>
            <VsCol>
                <VsTabs>
                    <VsTabItem title="First" active>
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
</docs>
