import { Button } from '@chakra-ui/react';
import SideBarWithHeader from "./shared/SideBar.jsx";
import {useEffect} from 'react';
import {getCustomers} from "./services/client.js";

const App = () => {

    useEffect(() => {
        getCustomers().then(res => {
            console.log(res)
        }).catch(err => {
            console.log(err)
        })
    },[])

    return (
        <SideBarWithHeader>
            <Button colorScheme='teal' variant='outline'>Click me</Button>
        </SideBarWithHeader>
    )
}

export default App;