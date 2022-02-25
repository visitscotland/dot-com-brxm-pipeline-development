import React, { Component } from "react"
import RsgPlayground from "rsg-components-default/Playground/Playground"

import FullWidthViewWrapper from "../FullWidthViewWrapper"

export default function Playground(props) {
  return React.createElement(FullWidthViewWrapper, {}, [
    React.createElement(RsgPlayground, { ...props, key: 1 }),
  ])
}
