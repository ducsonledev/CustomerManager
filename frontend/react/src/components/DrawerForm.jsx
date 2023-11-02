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
const CloseIcon = () => "x"

const DrawerForm = ({fetchCustomers}) => {
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
              <DrawerHeader>Create new Customer</DrawerHeader>
              <DrawerBody>
                <CreateCustomerForm 
                   fetchCustomers={fetchCustomers}
                />
              </DrawerBody>
              <DrawerFooter>
                <Button
                    leftIcon={<CloseIcon/>}
                    colorScheme={"teal"}
                    onClick={onClose}>
                  Close
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