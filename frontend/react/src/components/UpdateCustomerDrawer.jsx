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
import UpdateCustomerForm from './UpdateCustomerForm.jsx';


const AddIcon = () => "+"
const CloseIcon = () => "x"

const DrawerEditForm = ({id, name, email, age, gender, fetchCustomers}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()

    return (
        <>
        <Button
            colorScheme={"blue"}
            borderRadius={'3xl'}
            onClick={onOpen}
        >
            Edit Customer
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={'xl'}>
            <DrawerOverlay />
            <DrawerContent>
            <DrawerCloseButton />
            <DrawerHeader>Edit '{name}'</DrawerHeader>
            <DrawerBody>
                <UpdateCustomerForm
                    id={id}
                    name={name}
                    email={email}
                    age={age}
                    gender={gender}
                    fetchCustomers={fetchCustomers}
                ></UpdateCustomerForm>
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

export default DrawerEditForm;