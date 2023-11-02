import { Wrap,
         WrapItem,
         Spinner,
         Text
} from '@chakra-ui/react';
import SideBarWithHeader from "./components/shared/SideBar.jsx";
import SocialProfileWithImage from "./components/Card.jsx"
import { useEffect, useState } from 'react';
import { deleteCustomer, getCustomers } from "./services/client.js";
import DrawerForm from "./components/DrawerForm.jsx"
import { errorNotification } from "./services/notification.js"

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("")

    const fetchCustomers = () => {
        setLoading(true);
        getCustomers().then(res => {
            setCustomers(res.data)
        }).catch(err => {
            setError(err.response.data.message)
            errorNotification(
                err.code,
                err.response.data.message
            )
        }).finally(() => {
            setLoading(false)
        })
    }

    const handleDeleteCustomer = (id) => {
        deleteCustomer(id)
    }

    useEffect(() => {
        fetchCustomers();
    }, [])


    if (loading) {
        return (
            <SideBarWithHeader>
                <Spinner
                  thickness='4px'
                  speed='0.65s'
                  emptyColor='gray.200'
                  color='blue.500'
                  size='xl'
                />
            </SideBarWithHeader>
        )
    }

    if(err) {
        return (
            <SideBarWithHeader>
                 <DrawerForm 
                    fetchCustomers={fetchCustomers}
                 />
                <Text mt={5}>Oops, there was an error!</Text>
            </SideBarWithHeader>
        )
    }

    if(customers.length <= 0) {
        return (
            <SideBarWithHeader>
                 <DrawerForm 
                    fetchCustomers={fetchCustomers}
                 />
                <Text mt={5}>No customers available!</Text>
            </SideBarWithHeader>
        )
    }

    return (
        <SideBarWithHeader>
            <DrawerForm  fetchCustomers={fetchCustomers} />
            <Wrap justify='center' spacing='30px'>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <SocialProfileWithImage 
                            {...customer} 
                            imageNumber={index}
                            handleDeleteCustomer={() => {handleDeleteCustomer(customer.id)}}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SideBarWithHeader>
    )
}

export default App;