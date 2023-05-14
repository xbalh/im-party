interface formComponentGroup {
  name: string,
  children: formComponent[]
}

interface formComponent {
  [key: string]: any,

  id?: string,
  name: string,
  value: string,
  type: 'textField' | 'number' | 'switch' | 'button' | 'textArea' | 'select' | 'date' | 'time' | 'checkbox' | 'upload' | 'user' | 'role' | 'flexRow' | 'radio'
  config: formComponentConfig
}

type formComponentConfig =
  Record<string, any>
  |
  (
    VSelect
    & VTextField
    & VBtn
    & VCheckbox
    & VRadio
    & {
    options: formOption[],
    formChildren: formComponent[]
  }
    )


interface formOption {
  label?: string,
  value?: string
}

type formComponentType = NonNullable<formComponent['type']>

