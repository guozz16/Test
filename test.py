from power_env import PowerSys
from RL_brain import DeepQNetwork
import matplotlib.pyplot as plt
import tensorflow as tf

tf.reset_default_graph()

env = PowerSys()
env.reset()

RL = DeepQNetwork(n_actions=len(env.action_space),
                  n_features=len(env.state),
                  learning_rate=0.01, e_greedy=0.9,
                  replace_target_iter=100, memory_size=2000,
                  e_greedy_increment=0.0006,)

total_steps = 0


loss = [env.state['loss']]

for i_episode in range(10):

    observation = env.reset()
    ep_r = 0
    while True:

        action = RL.choose_action(observation)

        observation_, reward, done = env.step(action)

        RL.store_transition(observation, action, reward, observation_)

        ep_r += reward
        if total_steps > 600:
            RL.learn()

        if done:
            print('episode: ', i_episode,
                  'ep_r: ', round(ep_r, 2),
                  'epsilon:',round(RL.epsilon,2),
                  ' loss: ', env.state['loss'])
            loss.append(env.state['loss'])
            break

        observation = observation_
        total_steps += 1

# plot loss and cost
fig = plt.figure()
plt.plot(loss)
# plt.show()
# save rather than show
fig.savefig('res/learncurve.png', dpi=300)
