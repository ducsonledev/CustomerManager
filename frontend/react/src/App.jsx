import { Button, Spinner, Text } from '@chakra-ui/react';
import SideBarWithHeader from "./shared/SideBar.jsx";
import {useEffect, useState} from 'react';
import {getCustomers} from "./services/client.js";

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getCustomers().then(res => {
            setCustomers(res.data)
        }).catch(err => {
            console.log(err)
        }).finally(() => {
            setLoading(false)
        }
    )},[])

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

    if(customers.length <= 0) {
        return (
            <SideBarWithHeader>
                <Text>No customers available!</Text>
            </SideBarWithHeader>
        )
    }

    return (
        <SideBarWithHeader>
            {customers.map((customer, index) => (
                <p>{customer.name}</p>
            ))}
        </SideBarWithHeader>
    )
}

export default App;