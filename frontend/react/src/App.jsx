import { Wrap,
         WrapItem,
         Spinner,
         Text
} from '@chakra-ui/react';
import SideBarWithHeader from "./components/shared/SideBar.jsx";
import SocialProfileWithImage from "./components/Card.jsx"
import {useEffect, useState} from 'react';
import {getCustomers} from "./services/client.js";
import DrawerForm from "./components/DrawerForm.jsx"

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            getCustomers().then(res => {
                setCustomers(res.data)
            }).catch(err => {
                console.log(err)
            }).finally(() => {
                setLoading(false)
            })
        }, 3000)
    },[])

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
            <DrawerForm />
            <Wrap justify='center' spacing='30px'>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <SocialProfileWithImage {...customer} imageNumber={index}/>
                    </WrapItem>
                ))}
            </Wrap>
        </SideBarWithHeader>
    )
}

export default App;