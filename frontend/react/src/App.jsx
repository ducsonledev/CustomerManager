import UserProfile from "./UserProfile.jsx";

const user = [
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


function App() {
    const name = "John";
    return <div>
                <UserProfile
                    name={"John"}
                    age={22}
                    gender={"men"}>
                    <p>Hello</p>
                </UserProfile>
                <UserProfile
                    name={"Maria"}
                    age={23}
                    gender={"women"}>
                    <h1>Ciao</h1>
                </UserProfile>
                <p>test {name} </p>
                <h1>hello {name}</h1>
           </div>
}

export default App;
