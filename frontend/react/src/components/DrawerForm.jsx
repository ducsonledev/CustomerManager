import {Button,
        Drawer,
        DrawerOverlay,
        DrawerContent,
        DrawerCloseButton,
        DrawerHeader,
        DrawerBody,
        DrawerFooter,
        Input,
        useDisclosure
} from "@chakra-ui/react"
import CreateCustomerForm from './CreateCustomerForm.jsx';


const AddIcon = () => "+"

const DrawerForm = () => {
    const { isOpen, onOpen, onClose } = useDisclosure()

    return (
        <>
        <Button
            leftIcon={<AddIcon/>}
            colorScheme={"teal"}
            onClick={onOpen}
        >
            Create Customer
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={'xl'}>
                <DrawerOverlay />
                <DrawerContent>
                  <DrawerCloseButton />
                  <DrawerHeader>Create your account</DrawerHeader>

                  <DrawerBody>
                    <CreateCustomerForm />
                  </DrawerBody>
                  <DrawerFooter>
                    <Button type='submit' form='my-form'>
                      Save
                    </Button>
                  </DrawerFooter>
                </DrawerContent>
              </Drawer>
        </>
    )
}

export default DrawerForm;

export const App = () => {
  return (
    <>
      <Button onClick={onOpen}>Open</Button>

    </>
  )
}