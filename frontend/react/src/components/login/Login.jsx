import {
  Alert,
  AlertIcon,
  Box,
  Button,
  Checkbox,
  Flex,
  Text,
  FormControl,
  FormLabel,
  Heading,
  Input,
  Stack,
  Image,
  Link
} from '@chakra-ui/react'
import {useAuth} from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {errorNotification} from "../../services/notification.js";
import { Formik, Form, useField } from 'formik'
import * as Yup from 'yup'

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

const LoginForm = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  return (
    <Formik
      validateOnMount={true}
      validationSchema={
        Yup.object({
          username: Yup.string()
            .email("Must be valid email")
            .required("Email is required"),
          password: Yup.string()
            .max(20, "Password cannot be more than 20 characters")
            .required("Password is required")
        })
      }
      initialValues={{username:'', password: ''}}
      onSubmit={(values, {setSubmitting}) => {
        setSubmitting(true);
          login(values).then(res => {
              navigate("/dashboard")
              console.log("Successfully logged in", res);
          }).catch(err => {
              errorNotification(
                  err.code,
                  err.response.data.message
              )
          }).finally(() => {
              setSubmitting(false);
          })
      }}>
      {({isValid, dirty, isSubmitting}) => (
        <Form>
          <Stack mt={15} spacing={15}>
            <MyTextInput
              label={"Email"}
              name={"username"}
              type={"email"}
              placeholder={"hello@user.com"}
            />
            <MyTextInput
              label={"Password"}
              name={"password"}
              type={"password"}
              placeholder={"Type your password"}
            />
            <Button
              type={"submit"}
              isDisabled={!isValid || !dirty || isSubmitting}>
              Login
            </Button>
            </Stack>
        </Form>
      )}
    </Formik>
  )
}

const Login = () => {
  const { customer } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (customer) {
        navigate("/dashboard/customers");
    }
  })

  return (
    <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
      <Flex p={8} flex={1} align={'center'} justify={'center'}>
        <Stack spacing={4} w={'full'} maxW={'md'}>
          <Image
              src={"https://user-images.githubusercontent.com/40702606/210880158-e7d698c2-b19a-4057-b415-09f48a746753.png"}
              boxSize={"200px"}
              alt={"Amigoscode Logo"}
              alignSelf={"center"}
          />
          <Heading fontSize={'2xl'} mb={3}>Sign in to your account</Heading>
          <LoginForm />
        </Stack>
      </Flex>
      <Flex flex={1}
            p={10}
            flexDirection={"column"}
            alignItems={"center"}
            justifyContent={"center"}
            bgGradient={{sm: 'linear(to-r, blue.600, purple.600)'}}>
      <Text fontSize={"6xl"} color={'white'} fontWeight={"bold"} mb={5}>
            <Link target={"_blank"} href={"https://www..de/"}>
                Sign Up Now!
            </Link>
        </Text>
        <Image
          alt={'Login Image'}
          objectFit={'cover'}
          src={
            'https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1352&q=80'
          }
        />
      </Flex>
    </Stack>
  )
}

export default Login