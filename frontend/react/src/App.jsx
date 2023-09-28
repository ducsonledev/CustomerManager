import UserProfile from "./UserProfile.jsx";

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
    return (
        <UserProfiles users={users}/>
    )
}
export default App;
