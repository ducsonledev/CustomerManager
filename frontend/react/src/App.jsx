import UserProfile from "./UserProfile.jsx";
import {useState} from 'react'

const users = [
    {
        name: "Jamila",
        age: 22,
        gender: "FEMALE"
    },
    {
        name: "Anne",
        age: 41,
        gender: "FEMALE"
    },
    {
        name: "Rex",
        age: 22,
        gender: "MALE"
    },
    {
        name: "Moos",
        age: 22,
        gender: "IT"
    }
]

const UserProfiles = ({users}) => (
    <div>
        {users.map((user, index) => (
              <UserProfile
                  name={user.name}
                  age={user.age}
                  gender={user.gender}
                  imageNumber={index}
              />
          ))}
   </div>
)

function App() {

    const [counter, setCounter] = useState(0);

    return (
        <div>
            <button onClick={() => setCounter(prevCounter => prevCounter + 1)}>
                Increment Counter
            </button>
            <h1>{counter}</h1>
            <UserProfiles users={users}/>
        </div>
    )
}
export default App;
