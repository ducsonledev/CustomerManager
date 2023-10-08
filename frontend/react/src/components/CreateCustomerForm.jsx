import React from 'react';
import ReactDOM from 'react-dom';
import { Box,
         Button,
         Input,
         Alert,
         AlertIcon,
         Select,
         Stack,
         FormLabel
} from '@chakra-ui/react'
import { Formik,
         Form,
         useField
} from 'formik';
import * as Yup from 'yup';

const MyTextInput = ({ label, ...props }) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Input className="text-input" {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
            <AlertIcon/>
            {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};

const MySelect = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Select {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
            <AlertIcon/>
            {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};

// And now we can use these
const CreateCustomerForm = () => {
  return (
    <>
      <Formik
        initialValues={{
          name: '',
          email: '',
          age: 0,
          gender: '', // added for our select
        }}
        validationSchema={Yup.object({
          name: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .required('Required'),
          email: Yup.string()
            .email('Invalid email address')
            .required('Required'),
          age: Yup.number()
            .min(16, 'Must be at least 16 years of age')
            .max(100, 'Must be less than 100 years of age')
            .required('Required'),
          gender: Yup.string()
            .oneOf(
              ['MALE', 'FEMALE'],
              'Invalid Gender'
            )
            .required('Required'),
        })}
        onSubmit={(values, { setSubmitting }) => {
          setTimeout(() => {
            alert(JSON.stringify(values, null, 2));
            setSubmitting(false);
          }, 400);
        }}
      >
        {({isValid, isSubmitting}) => (
            <Form>
                <Stack spacing={"24px"}>
                  <MyTextInput
                    label="Name"
                    name="name"
                    type="text"
                    placeholder="Jane"
                  />

                  <MyTextInput
                    label="Email Address"
                    name="email"
                    type="email"
                    placeholder="jane@formik.com"
                  />

                  <MyTextInput
                    label="Age"
                    name="age"
                    type="number"
                    placeholder="20"
                  />

                  <MySelect label="Gender" name="gender">
                    <option value="">Select a gender</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                  </MySelect>

                  <Button disabled={!isValid || isSubmitting} type="submit">Submit</Button>
               </Stack>
            </Form>
        )}
      </Formik>
    </>
  );
};

export default CreateCustomerForm;