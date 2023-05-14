const formComponents: (primary: string) => formComponentGroup[] = (primary: string) => [
  {
    name: 'Base',
    children: [
      {
        name: 'textField',
        value: 'textField',
        type: 'textField',
        config:
          {variant: 'outlined'}
      },
      {
        name: 'button',
        value: 'button',
        type: 'button',
        config: {
          color: primary
        }
      },
      {
        name: 'date',
        value: 'date',
        type: 'date',
        config: {
          variant: 'outlined'
        }
      },
      {
        name: 'time',
        value: 'time',
        type: 'time',
        config: {
          variant: 'outlined'
        }
      },
      {
        name: 'number',
        value: 'number',
        type: 'number',
        config: {
          variant: 'outlined'
        }
      },
      {
        name: 'textarea',
        value: 'textarea',
        type: 'textArea',
        config: {
          variant: 'outlined'
        }
      },
      {
        name: 'upload',
        value: 'upload',
        type: 'upload',
        config: {
          variant: 'outlined'
        }
      },
      {
        name: 'select',
        value: 'select',
        type: 'select',
        config: {
          variant: 'outlined',
          options: [
            {
              label: 'option1',
              value: 'option1'
            },
            {
              label: 'option2',
              value: 'option2'
            }
          ]
        }
      },
      {
        name: 'checkbox',
        value: 'checkbox',
        type: 'checkbox',
        config: {
          options: [
            {
              label: 'option1',
              value: 'option1'
            },

            {
              label: 'option2',
              value: 'option2'
            }
          ]
        }
      },
      {
        name: 'radio',
        value: 'radio',
        type: 'radio',
        config: {
          options: [
            {
              label: 'option1',
              value: 'option1'
            },

            {
              label: 'option2',
              value: 'option2'
            }
          ]
        }
      }
    ]
  },
  {
    name: 'Advanced',
    children: [
      {
        name: 'user',
        value: 'user',
        type: 'user',
        config: {
          variant: 'outlined'
        }
      },
      {
        name: 'role',
        value: 'role',
        type: 'role',
        config: {
          variant: 'outlined'
        }
      }
    ]
  },
  {
    name: 'Layout',
    children: [
      {
        name: 'flexRow',
        value: 'flexRow',
        type: 'flexRow',
        config: {
          formChildren: []
        }
      }
    ]
  }
]

export default formComponents
