import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogContent,
    AlertDialogOverlay,
    AlertDialogCloseButton,
    Button,
    useDisclosure
  } from '@chakra-ui/react'
import React from 'react'
import { deleteCustomer } from '../services/client'
import { errorNotification, successNotification } from '../services/notification';

function DeleteCustomerButton({id, name, fetchCustomers}) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = React.useRef()
    const onDelete = () => {
        deleteCustomer(id).then(res => {
          console.log(res)
          successNotification(
            "Customer deleted",
            `${name} was successfully deleted`
          )
          fetchCustomers()
        }).catch(err => {
          console.log(err)
          errorNotification(
            err.Code,
            err.response.data.message
          )
        }).finally(() => {
          onClose()
        })
    }
    return (
      <>
        <Button colorScheme='red' borderRadius='3xl' onClick={onOpen}>
          Delete Customer
        </Button>
  
        <AlertDialog
          isOpen={isOpen}
          leastDestructiveRef={cancelRef}
          onClose={onClose}
        >
          <AlertDialogOverlay>
            <AlertDialogContent>
              <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                Delete '{name}'
              </AlertDialogHeader>
  
              <AlertDialogBody>
                Are you sure you want to delete {name}? You can't undo this action afterwards.
              </AlertDialogBody>
  
              <AlertDialogFooter>
                <Button ref={cancelRef} onClick={onClose}>
                  Cancel
                </Button>
                <Button 
                    colorScheme='red' 
                    onClick={onDelete} 
                    ml={3}>
                  Delete
                </Button>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialogOverlay>
        </AlertDialog>
      </>
    )
  }

  export default DeleteCustomerButton;