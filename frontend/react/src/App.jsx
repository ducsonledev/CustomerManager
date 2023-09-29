import { Button } from '@chakra-ui/react';
import SideBarWithHeader from "./shared/SideBar.jsx";

const App = () => {
    return (
        <SideBarWithHeader>
            <Button colorScheme='teal' variant='outline'>Click me</Button>
        </SideBarWithHeader>
    )
}

export default App;