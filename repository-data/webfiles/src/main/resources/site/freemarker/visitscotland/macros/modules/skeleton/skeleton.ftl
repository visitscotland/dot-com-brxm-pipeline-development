<#include "../../../../include/imports.ftl">

<#macro skeleton>
    <div 
        class="skeleton-site"
        style="opacity: 0.4;
            width: 100vw;
            height: 100vh;
            overflow: hidden;"
    >
        <div
            style="height: 28px;;
                background-color: #700e57;"
        ></div>
        <div
            style="height: 45px;
                background-color: #eeeeee"
        ></div>
        <div
            class="skeleton-site-shine"
            style="height: 100vh;
                background-color: #bbbbbb;
                position: relative;"
        >
        </div>
        <div
            style="position: absolute;
                bottom: 0;
                left: 10%;
                width: 80%;
                height: 120px;
                background-color: #ffffff;
                z-index: 2;"
        ></div>
    </div>

    <style>
        .skeleton-site-shine:after {
            content:'';
            top:0;
            left: 0;
            transform:translateX(100%);
            width:100%;
            height:100%;
            position: absolute;
            z-index:1;
            animation: skeleton-site-slide 3s infinite 6s;
            background: linear-gradient(90deg, #bbbbbb 0%, #ffffff 50%, #bbbbbb 100%);
        }
	

        /* animation */

        @keyframes skeleton-site-slide {
            0% {transform:translateX(-100%);}
            100% {transform:translateX(100%);}
        }
    </style>
</#macro>